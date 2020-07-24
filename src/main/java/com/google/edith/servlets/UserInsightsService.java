package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator; 
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * This class provides the functionality to parse specifc user data from 
 * datastore and send an agreggate of that information in a JSON file.
 */
public final class UserInsightsService implements UserInsightsInterface {
  
  private String userId;  
  private DatastoreService datastore;
  private final Gson GSON = new Gson();
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


  public UserInsightsService(String userId) {
    this.userId = userId;
    this.datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** This should only be called each time a new user makes an accout. */
  public void createUserStats() { 
    List<Key> items = new ArrayList<>();

    Entity userStats = new Entity("UserStats");
    userStats.setProperty("userId", userId);
    userStats.setProperty("Items", items);
    
    datastore.put(userStats);
  }

  /** 
   * Updates the Items list in the UserStats Entity in datastore
   * corresponding to this user.
   * @param items - Non-null list of item Keys to be added to the current Item list.
   */
  public void updateUserStats(List<Key> newItems) {
    Optional<Entity> userStatsContainer = retreiveUserStats();
    if (!userStatsContainer.isPresent()) {
        return;
    } 
    Entity userStats = userStatsContainer.get();   
    List<Key> items = (List<Key>) userStats.getProperty("Items");
    if (items == null) {
      items = newItems;
    } else {
      for (Key newItem : newItems) {
        if (!items.contains(newItem)) {
          items.add(newItem);
        }
      }
    }
    userStats.setProperty("Items", items);
    datastore.put(userStats);
  }
 
  /** 
   * Copmiles the spending using the Item list found in this user's
   * UserStats Entity in datastore.
   * TODO (malachibre): Allow for various time periods (only calculates weekly aggregate now).
   * @return A list of {@code WeekInfo} objects relating a time period to the spending in that time period.
   */
  public ImmutableList<WeekInfo> aggregateUserData() { 
    Optional<Entity> userStatsContainer = retreiveUserStats();
    if (!userStatsContainer.isPresent()) {
      return ImmutableList.copyOf(new ArrayList<WeekInfo>());
    }
    Entity userStats = userStatsContainer.get();
    List<Key> items = (List<Key>) userStats.getProperty("Items");
    if (items == null) {
      return ImmutableList.copyOf(new ArrayList<WeekInfo>());
    }

    /** Assumes dates are in the form yyyy-mm-dd */
    Comparator<Key> SORT_BY_DATE = (Key item1, Key item2) -> {
    try {
      return ((String) (datastore.get(item1).getProperty("date")))
        .compareTo((String) (datastore.get(item2).getProperty("date")));
      } catch (EntityNotFoundException | NullPointerException e )  {
        return 0;
      }
    };
    items.sort(SORT_BY_DATE);
    return calculateWeeklyTotal(items);
  }

  /**
   * Creates a Json string that contains the weekly aggregate for this user
   * and the items this user purchased.
   * @return a Json formatted String of items and an aggregate.
   */
  public String createJson() {
    List<WeekInfo> aggregateValues = aggregateUserData();
    String aggregateJson = GSON.toJson(aggregateValues);
    Optional<Entity> userStatsContainer = retreiveUserStats();

    if (!userStatsContainer.isPresent()){
      return GSON.toJson(createDefaultMap()); 
    }

    List<Key> itemKeys = (List<Key>) retreiveUserStats().get()
                                        .getProperty("Items");
    // Each item is mapped to an Item object to make their 
    // properties parseable by GSON.
    List<Item> items = itemKeys.stream()
                         .map(key ->{
                            try {
                              Entity item = datastore.get(key);
                              return new Item(
                                (String) item.getProperty("name"),
                                (Double) item.getProperty("price"),
                                (long) item.getProperty("quantity"),
                                (String) item.getProperty("date")
                              );
                            } catch (EntityNotFoundException e) {
                              return null;
                            }
                          })
                          .collect(Collectors.toList());
    String itemsJson = GSON.toJson(items);
    JsonObject userJson = new JsonObject();
    userJson.addProperty("weeklyAggregate", aggregateJson);
    userJson.addProperty("items", itemsJson);
    return GSON.toJson(userJson);
  }

  /**
   * Finds the UserStats entity corresponding to {@code userId} in datastore
   * and returns this entity contained inside an Optional.
   * @return UserStats entity for this user
   */
  public Optional<Entity> retreiveUserStats() {
    if (userId == null) {
      return Optional.empty();
    }
    Filter idFilter = new FilterPredicate("userId", 
                                FilterOperator.EQUAL,
                                userId);
    Query query = new Query("UserStats").setFilter(idFilter);
    PreparedQuery results = datastore.prepare(query);
    List<Entity> entities = results.asList(FetchOptions.Builder.withLimit(10));
    return entities.isEmpty() ? Optional.empty() 
                              : Optional.of(entities.get(0));
  }

  /**
   * Creates a List of {@code WeekInfo} relating string weekly period keys to string spending values.
   * This method calculates the trailing based on a date. 
   * Example: {"06-20-2020" : "53.0"}
   * @param items - A list of {@code WeekInfo} objects that reference Item entities
                  - in the datastore.
   * @return Creates a List of WeekInfo objects relating the each ending day of a weekly period and  
   *         values for the total spending during that period. 
   * TODO (malachibre) : Modify this method by using an enum to calculate time
   *                     period totals using an enum.
   */
  private ImmutableList<WeekInfo> calculateWeeklyTotal(List<Key> itemKeys)  {
    if (itemKeys == null || itemKeys.isEmpty()) {
      return ImmutableList.copyOf(new ArrayList<WeekInfo>());
    }
    List<WeekInfo> weeklyTotals = new ArrayList<WeekInfo>();
    LocalDate currentEndOfWeek;
    try {
      currentEndOfWeek = getEndOfWeek(LocalDate.parse((String) datastore.get(itemKeys.get(0))
                                          .getProperty("date"), DATE_FORMATTER));
    } catch (EntityNotFoundException e) {
      System.err.println("Error: Entity could not be located");
      return ImmutableList.copyOf(new ArrayList<WeekInfo>());
    }

    double weeklyTotal = 0;
    for (Key itemKey : itemKeys) {
      try {
        Entity item = datastore.get(itemKey);
        LocalDate itemDate = LocalDate.parse((String) item.getProperty("date"),
                                             DATE_FORMATTER);

        // If there is a positive amount of time between
        // {@code currentEndOfWeek} and {@code itemDate}
        // that means that itemDate is after currentEndOfWeek and 
        // currentEndOfWeek needs to be updated.
        if (ChronoUnit.DAYS.between(currentEndOfWeek, itemDate) > 0) {
          weeklyTotals.add(new WeekInfo(currentEndOfWeek.toString(), Double.toString(weeklyTotal)));
          currentEndOfWeek = getEndOfWeek(itemDate);
          weeklyTotal = 0;
        }

        weeklyTotal += ((double) item.getProperty("price")) * 
                           ((long) item.getProperty("quantity"));
    
      } catch (EntityNotFoundException e) {
        System.err.println("Error: Entity could not be found");
      } 
    }
    

    weeklyTotals.add(new WeekInfo(currentEndOfWeek.toString(), Double.toString(weeklyTotal)));

    return ImmutableList.copyOf(weeklyTotals);
  }

  /**
   * Finds the Date for the last day of the week that {@code itemDate} is in 
   * (weeks start on Monday and end on Sunday).
   * @param itemDate - Uses the int day value of the week from this 
   *                   paramter to calculate the end of its week.
   * @return the LocalDate representing the end of the week {@code itemDate}
   *         is in.
   */
  private LocalDate getEndOfWeek(LocalDate itemDate) {
    int currentDayOfWeek = itemDate.getDayOfWeek().getValue();
    return itemDate.plusDays(7 - currentDayOfWeek);
  }  

  /**
   * Creates a default map when required userStats properties are not found.
   * @return A map with the string keys: weeklyAggregate and items that map to
   *         to empty string values.
   */
  private ImmutableMap<String, String> createDefaultMap() {
    Map<String, String> emptyMap = new LinkedHashMap<String, String>();
    emptyMap.put("weeklyAggregate", "");
    emptyMap.put("items", "");
    return ImmutableMap.copyOf(emptyMap);
  }
}

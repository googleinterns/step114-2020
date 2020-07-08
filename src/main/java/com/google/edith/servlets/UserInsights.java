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
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides the funttionality to parse specifc user data from 
 * datastore and send an agreggate of that information in a JSON file.
 */
public final class UserInsights {

  private String userId;

  private static final DatastoreService datastore = 
      DatastoreServiceFactory.getDatastoreService();  
  private static final DateTimeFormatter dateFormatter = 
      DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final Gson gson = new Gson();

  public UserInsights(String userId) {
    this.userId = userId;
  }

  /**
   * Retrieves the Item entities corresponding to {@code userId} in datastore.
   * @return List of Item entities for this user
   */
  private List<Entity> retreiveUserItems() {
    Filter idFilter = new FilterPredicate("userId", 
                                FilterOperator.EQUAL,
                                userId);
    Query query = new Query("Item").setFilter(idFilter)
                                    .addSort("date", SortDirection.ASCENDING);
    PreparedQuery results = datastore.prepare(query);
    return results.asList(FetchOptions.Builder.withLimit(Integer.MAX_VALUE));
  }

  /**
   * Retrieves the UserStats entitie corresponding to {@code userId} in datastore.
   * @return UserStats entity for this user
   */ 
  private Entity retreiveUserStats() {
    Filter idFilter = new FilterPredicate("userId", 
                                FilterOperator.EQUAL,
                                userId);
    Query query = new Query("UserStats").setFilter(idFilter);
    PreparedQuery results = datastore.prepare(query);
    return results.asList(FetchOptions.Builder.withLimit(10)).get(0);
  }

  /** 
   * This should only be called each time a new user makes an accout
   * and uploads a new receipt for the first time. 
   */
  public void createUserStats() { 
     Entity userStats = new Entity("UserStats");
     userStats.setProperty("userId", userId);
     userStats.setProperty("weekEndDates", new HashSet<String>());
     userStats.setProperty("weeklyTotals", new ArrayList<String>());
     datastore.put(userStats);
  }

  /** 
   * Updates the weekEndDates and weeklyTotals properties for this user
   * each time a new receipt is added.
   */
  public void updateUserStats() {
    Map<String, String> weeklyTotals = aggregateUserData();
    Entity userStats = retreiveUserStats();
    userStats.setProperty("weekEndDates", weeklyTotals.keySet());
    userStats.setProperty("weeklyTotals", weeklyTotals.values());
    datastore.put(userStats);
  }
 
  /** 
   * Copmiles the spending using the Item list found in this user's
   * UserStats Entity in datastore.
   * TODO: Allow for various time periods (only calculates weekly aggregate now).
   * @return A map relating a time period to the spending in that time period.
   */
  public Map<String, String> aggregateUserData() { 
    List<Entity> items = retreiveUserItems();
    if (items == null || items.isEmpty()) {
        return null;
    }
    return calculateWeeklyTotal(items);
  }

  /**
   * Creates a String-String map relating weekly periods to spending.
   * @param items - A list of {@code Key} objects that reference Item entities
                  - in the datastore.
   * @return Creates a map with keys for each ending day of a weekly period and  
   *         values for the total spending during that period. 
   */
  public Map<String, String> calculateWeeklyTotal(List<Entity> items)  {
    Map<String, String> weeklyTotals = new HashMap<String, String>();
    LocalDate currentEndOfWeek = getEndOfWeek(LocalDate.parse((String) 
                            items.get(0).getProperty("date"), dateFormatter));
    double weeklyTotal = 0;
    Entity itemEntity;
    for(Entity item : items) {
      LocalDate itemDate = LocalDate.parse((String) item.getProperty("date"),
                                             dateFormatter);
      
      if(ChronoUnit.DAYS.between(itemDate, currentEndOfWeek) < 0) {
        weeklyTotals.put(currentEndOfWeek.toString(), Double.toString(weeklyTotal));
        currentEndOfWeek = getEndOfWeek(itemDate);
        weeklyTotal = 0;
      }

      weeklyTotal += ((double) item.getProperty("price")) * 
                        ((long) item.getProperty("quantity"));
    
    }
    
    weeklyTotals.put(currentEndOfWeek.toString(), Double.toString(weeklyTotal));
    return weeklyTotals;
  }

  /**
   * Finds the Date for the last day of the week that {@code itemDate} is in 
   * (weeks start on Monday and end on Sunday).
   * @param itemDate - Uses the int day value of the week from this 
   *                   paramter to calculate the end of its week.
   * @return the LocalDate representing the end of the week {@code itemDate}
   *         is in.
   */
  public LocalDate getEndOfWeek(LocalDate itemDate) {
    int currentDayOfWeek = itemDate.getDayOfWeek().getValue();
    return itemDate.plusDays(7 - currentDayOfWeek);
  }  
  
  /**
   * Creates a Json string that contains the weekly aggregate for this user
   * and the items this user purchased.
   * @return a Json formatted String of items and an aggregate.
   */
  public String createJson() {
    Map<String, String> aggregateValues = aggregateUserData();
    String aggregateJson =  gson.toJson(aggregateValues);
    List<Entity> itemEntities = retreiveUserItems();
    List<Item> items = itemEntities.stream()
                         .map(item ->{
                            return new Item(
                              (Double) item.getProperty("price"),
                              (long) item.getProperty("quantity"),
                              (String) item.getProperty("date")
                            );
                            })
                            .collect(Collectors.toList());
    String itemsJson = gson.toJson(items);
    JsonObject userJson = new JsonObject();
    userJson.addProperty("weeklyAggregate", aggregateJson);
    userJson.addProperty("items", itemsJson);
    return gson.toJson(userJson);
  }
}

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class UserInsights {

  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();  
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final Gson gson = new Gson();
  private static String userId;

  private static Comparator<Key> SORT_BY_DATE = (Key item1, Key item2) -> {
      try {
        return ((String) (datastore.get(item1).getProperty("date")))
            .compareTo((String) (datastore.get(item2).getProperty("date")));
      } catch(EntityNotFoundException e)  {
        return 0;
      }
  };
  

  public UserInsights(String userId) {
    this.userId = userId;
  }

  public void createUserStats() { 
    List<Key> items = new ArrayList<>();

    Entity newUserStats = new Entity("UserStats");

    items.add(null);

    newUserStats.setProperty("userId", userId);
    newUserStats.setProperty("items", items);

    datastore.put(newUserStats);
  }

  /** This method is called after a UserStats Entity has been 
   * created for the current User and they are attempting to upload
   * a new receipt.
   */
  public void updateUserStats(List<Key> newItems) {
    Filter idFilter = new FilterPredicate("userId", 
                                FilterOperator.EQUAL,
                                userId);
    Query query = new Query("UserStats").setFilter(idFilter);
    PreparedQuery results = datastore.prepare(query);
    Entity userStats = results.asList(FetchOptions.Builder.withLimit(10))
                              .get(0);

    List<Key> items = (List<Key>) userStats.getProperty("Items");
    items.addAll(newItems);
    userStats.setProperty("Items", items);
    datastore.put(userStats);

  }

  public void aggregateUserData() { 
    Filter idFilter = new FilterPredicate("userId", 
                                FilterOperator.EQUAL,
                                userId);
    Query query = new Query("UserStats").setFilter(idFilter);                                   
    PreparedQuery results = datastore.prepare(query);
    Entity userStats = results.asList(FetchOptions.Builder.withLimit(10))
                              .get(0);
    List<Key> items = (List<Key>) userStats.getProperty("Items");

    items.sort(SORT_BY_DATE);

    calculateWeeklyTotal(items);

  }


  public Map<String, Integer> calculateWeeklyTotal(List<Key> items)  {
    
    Map<String, Integer> weeklyTotals = new HashMap<String, Integer>();
    LocalDate currentStartOfWeek;
    try {
      currentStartOfWeek = LocalDate.parse((String) datastore.get(items.get(0))
                                            .getProperty("date"), dateFormatter);
    } catch(EntityNotFoundException e) {
      throw new IllegalArgumentException();
    }
    int weeklyTotal = 0;
    Entity itemEntity;
    for(Key item : items) {
      try {
        itemEntity = datastore.get(item);
      } catch(EntityNotFoundException e) {
        throw new IllegalArgumentException();
      }
      LocalDate itemDate = LocalDate.parse((String) itemEntity.getProperty("date"),
                                           dateFormatter);
      if(ChronoUnit.DAYS.between(currentStartOfWeek, itemDate) > 6) {
        currentStartOfWeek = getStartOfWeek(itemDate);
        weeklyTotals.put(currentStartOfWeek.toString(), weeklyTotal);
        weeklyTotal = 0;
      }

      weeklyTotal += ((int) itemEntity.getProperty("price")) * 
                         ((int) itemEntity.getProperty("quantity")); 

    }

    return weeklyTotals;
  }

  public LocalDate getStartOfWeek(LocalDate itemDate) {
    int currentDayOfWeek = itemDate.getDayOfWeek().getValue();
    return itemDate.minusDays(currentDayOfWeek);
  }

   
  
}

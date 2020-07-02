package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Assert;
import java.time.format.FormatStyle;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import com.google.sps.servlets.UserInsights;


public final class UserInsightsTest {
  private static final String USER_ID = "userId";
  private static UserInsights userInsights;
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final LocalServiceTestHelper testHelper = 
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    
  @Before
  public void setUp() {
    userInsights = new UserInsights(USER_ID);
    testHelper.setUp();    
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Test
  public void creatUserInisghts() {
    // A new UserStats Entity should be added to the datastore
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    userInsights.createUserStats();
    Assert.assertEquals(1, datastore.prepare(new Query("UserStats")).countEntities());
  }

  @Test
  public void correctUserId() {
    // Checks to see if the right UserId was added to the datastore  
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    userInsights.createUserStats();
    Assert.assertEquals(USER_ID, datastore.prepare(new Query("UserStats"))
                                   .asList(FetchOptions.Builder.withLimit(10))
                                   .get(0).getProperty("UserId"));
  }

  @Test 
  public void correctDefaultItems() {
    // Checks to see if the right UserId was added to the datastore  
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    userInsights.createUserStats();
    Assert.assertEquals(null, datastore.prepare(new Query("UserStats"))
                                   .asList(FetchOptions.Builder.withLimit(10))
                                   .get(0).getProperty("Items"));
  }

  @Test
  public void updateUserStatsWhenDefault() {
    // The Items property in thie UserStats entity should be equal to 
    // {@code items}
    List<Key> items = new ArrayList<Key>();
    for(int i = 0; i < 5; i++) {
       items.add(KeyFactory.createKey("Item", "Item" + i));
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    userInsights.createUserStats();
    userInsights.updateUserStats(items);
    Assert.assertEquals(items, datastore.prepare(new Query("UserStats"))
                                   .asList(FetchOptions.Builder.withLimit(10))
                                   .get(0).getProperty("Items"));

  }

  @Test
  public void updateUserStatsWhenNonEmpty() {
    // The Items property in thie UserStats entity should be equal to 
    // {@code items} + {@code items2}
    List<Key> items = new ArrayList<Key>();
    List<Key> items2 = new ArrayList<Key>();
    for(int i = 0; i < 5; i++) {
      items.add(KeyFactory.createKey("Item", "Item" + i));
    }
    for(int i = 5; i < 10; i++) {
      items2.add(KeyFactory.createKey("Item", "Item" + i));
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    userInsights.createUserStats();
    userInsights.updateUserStats(items);
    userInsights.updateUserStats(items2);
    items.addAll(items2);
    Assert.assertEquals(items, datastore.prepare(new Query("UserStats"))
                                   .asList(FetchOptions.Builder.withLimit(10))
                                   .get(0).getProperty("Items"));

  }

  @Test
  public void aggregateData() {
    // The Items property in thie UserStats entity should be equal to 
    // {@code items}
        
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();   
    
    List<Key> items = new ArrayList<>();
    items.add(KeyFactory.createKey("Item", "Item1"));
    items.add(KeyFactory.createKey("Item", "Item2"));
    items.add(KeyFactory.createKey("Item", "Item3"));
    items.add(KeyFactory.createKey("Item", "Item4"));

    Entity newEntity = new Entity(items.get(0));

    newEntity.setProperty("price", 5.00);
    newEntity.setProperty("quantity", 1);
    newEntity.setProperty("date", "2020-06-29");
    datastore.put(newEntity); 

    Entity newEntity2 = new Entity(items.get(1));

    newEntity2.setProperty("price", 6.00);
    newEntity2.setProperty("quantity", 2);
    newEntity2.setProperty("date", "2020-06-30");
    datastore.put(newEntity2);  

    Entity newEntity3 = new Entity(items.get(2));

    newEntity3.setProperty("price", 7.00);
    newEntity3.setProperty("quantity", 3);
    newEntity3.setProperty("date", "2020-07-06"); 
    datastore.put(newEntity3);  

    Entity newEntity4 = new Entity(items.get(3));

    newEntity4.setProperty("price", 8.00);
    newEntity4.setProperty("quantity", 4);
    newEntity4.setProperty("date", "2020-07-06");
    datastore.put(newEntity4);  

    Map<String, Double> map = new HashMap<String, Double>();
    map.put("2020-07-04", 17.00);
    map.put("2020-07-11", 53.00);
    userInsights.createUserStats();
    userInsights.updateUserStats(items);
   
    Assert.assertEquals(map, userInsights.aggregateUserData());
   
  }

}
package com.google.edith;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.edith.servlets.UserInsights;
import com.google.edith.servlets.Item;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;

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
    Assert.assertEquals(1, datastore.prepare(new Query("UserStats"))
                                    .countEntities(FetchOptions.Builder
                                                            .withLimit(10)));
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
    List<Key> items = new ArrayList<>();
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
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();   
    
    List<Key> items = new ArrayList<>();

    items.add(KeyFactory.createKey("Item", "Item1"));
    items.add(KeyFactory.createKey("Item", "Item2"));
    items.add(KeyFactory.createKey("Item", "Item3"));
    items.add(KeyFactory.createKey("Item", "Item4"));

    // The week containing {@code Entity} and {@code Entity}
    // concludes on 07-05-2020 and the total should be 17.00.
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

    // The week containing {@code Entity3} and {@code Entity4}
    // concludes on 07-12-2020 and the total should be 53.00.
    Entity newEntity3 = new Entity(items.get(2));
    newEntity3.setProperty("price", 7.00);
    newEntity3.setProperty("quantity", 3);
    newEntity3.setProperty("date", "2020-07-11"); 
    datastore.put(newEntity3);  

    Entity newEntity4 = new Entity(items.get(3));
    newEntity4.setProperty("price", 8.00);
    newEntity4.setProperty("quantity", 4);
    newEntity4.setProperty("date", "2020-07-12");
    datastore.put(newEntity4);  

    Map<String, Double> map = new HashMap<String, Double>();
    map.put("2020-07-05", 17.00);
    map.put("2020-07-12", 53.00);
    userInsights.createUserStats();
    userInsights.updateUserStats(items);
   
    Assert.assertEquals(map, userInsights.aggregateUserData());
  }

//   @Test
//   public void createJson() {    
//     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();   
    
//     List<Key> items = new ArrayList<>();
//     List<Item> itemProperties = new ArrayList<>();

//     items.add(KeyFactory.createKey("Item", "Item1"));
//     items.add(KeyFactory.createKey("Item", "Item2"));
//     items.add(KeyFactory.createKey("Item", "Item3"));
//     items.add(KeyFactory.createKey("Item", "Item4"));

//     Entity newEntity = new Entity(items.get(0));
//     newEntity.setProperty("price", 5.00);
//     newEntity.setProperty("quantity", 1);
//     newEntity.setProperty("date", "2020-06-29");
//     itemProperties.add(new Item(5.00, 1L, "2020-06-29"));
//     datastore.put(newEntity); 

//     Entity newEntity2 = new Entity(items.get(1));
//     newEntity2.setProperty("price", 6.00);
//     newEntity2.setProperty("quantity", 2);
//     newEntity2.setProperty("date", "2020-06-30");
//     itemProperties.add(new Item(6.00, 2L, "2020-06-30"));
//     datastore.put(newEntity2); 
    
//     Entity newEntity3 = new Entity(items.get(2));
//     newEntity3.setProperty("price", 7.00);
//     newEntity3.setProperty("quantity", 3);
//     newEntity3.setProperty("date", "2020-07-11");
//     itemProperties.add(new Item(7.00, 3L, "2020-07-11"));
//     datastore.put(newEntity3);  

//     Entity newEntity4 = new Entity(items.get(3));
//     newEntity4.setProperty("price", 8.00);
//     newEntity4.setProperty("quantity", 4);
//     newEntity4.setProperty("date", "2020-07-12");
//     itemProperties.add(new Item(8.00, 4L, "2020-07-12"));
//     datastore.put(newEntity4);  

//     Map<String, Double> map = new HashMap<String, Double>();
//     String mapJson = new Gson().toJson(map);
//     map.put("2020-07-05", 17.00);
//     map.put("2020-07-12", 53.00);
//     userInsights.createUserStats();
//     userInsights.updateUserStats(items);

//     JsonObject testJson = new JsonObject();
//     testJson.addProperty("weeklyAggregate", mapJson);
//     String itemPropertiesJson = new Gson().toJson(items);
//     testJson.addProperty("items", itemPropertiesJson);
//     String expectedJson = new Gson().toJson(testJson);
   
//     Assert.assertEquals(expectedJson, userInsights.createJson());
//   }

}

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
import com.google.common.collect.ImmutableList;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.UserInsights;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public final class UserInsightsTest {
  private static final String USER_ID = "userId";
  private DatastoreService datastore;
  private static UserInsights userInsights;
  private final LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    testHelper.setUp();
    datastore = DatastoreServiceFactory.getDatastoreService();
    userInsights = new UserInsights(USER_ID);
    userInsights.createUserStats();
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Test
  public void creatUserStats_addsToDatstore() {
    // A new UserStats Entity should be added to the datastore
    Assert.assertEquals(
        1,
        datastore
            .prepare(new Query("UserStats"))
            .countEntities(FetchOptions.Builder.withLimit(10)));
  }

  @Test
  public void createUserStats_addsCorrectUserId() {
    // Checks to see if the right UserId was added to the datastore
    Assert.assertEquals(
        USER_ID,
        datastore
            .prepare(new Query("UserStats"))
            .asList(FetchOptions.Builder.withLimit(10))
            .get(0)
            .getProperty("userId"));
  }

  @Test
  public void createUserStats_addsCorrectItems() {
    // Checks to see if the right UserId was added to the datastore
    Assert.assertEquals(
        null,
        datastore
            .prepare(new Query("UserStats"))
            .asList(FetchOptions.Builder.withLimit(10))
            .get(0)
            .getProperty("Items"));
  }

  @Test
  public void updateUserStats_addsNewItems() {
    // The Items property in thie UserStats entity should be equal to
    // {@code items}
    List<Key> items = createTestKeyList(0, 5);
    userInsights.updateUserStats(items);
    Assert.assertEquals(
        items,
        datastore
            .prepare(new Query("UserStats"))
            .asList(FetchOptions.Builder.withLimit(10))
            .get(0)
            .getProperty("Items"));
  }

  @Test
  public void updateUserStats_whenNonEmpty_addsNewItems() {
    // The Items property in thie UserStats entity should be equal to
    // {@code items} + {@code items2}
    List<Key> items1 = createTestKeyList(0, 5);
    List<Key> items2 = createTestKeyList(5, 10);
    List<Key> items = new ArrayList<>(items1);
    items.addAll(items2);
    userInsights.updateUserStats(items1);
    userInsights.updateUserStats(items2);
    // items.addAll(items2);
    Assert.assertEquals(
        items,
        datastore
            .prepare(new Query("UserStats"))
            .asList(FetchOptions.Builder.withLimit(10))
            .get(0)
            .getProperty("Items"));
  }

  @Test
  public void aggregateUserData_withQuantityTwoOrLess_returnsCorrectJsonString() {
    List<Key> items = createTestKeyList(0, 2);

    Entity newEntity = new Entity(items.get(0));
    setEntityProperties(newEntity, 5, 1, "2020-06-29");
    datastore.put(newEntity);

    Entity newEntity2 = new Entity(items.get(1));
    setEntityProperties(newEntity2, 6, 2, "2020-06-30");
    datastore.put(newEntity2);

    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("2020-07-05", "17.0");

    userInsights.updateUserStats(items);

    Assert.assertEquals(map, userInsights.aggregateUserData());
  }

  @Test
  public void aggregateUserData_withQuantityMoreThanTwo_returnsCorrectJsonString() {
    List<Key> items = createTestKeyList(0, 4);
    List<Item> itemProperties = new ArrayList<>();

    Entity newEntity = new Entity(items.get(0));
    setEntityProperties(newEntity, 5, 1, "2020-06-29");
    datastore.put(newEntity);

    Entity newEntity2 = new Entity(items.get(1));
    setEntityProperties(newEntity2, 6, 2, "2020-06-30");
    datastore.put(newEntity2);

    Entity newEntity3 = new Entity(items.get(2));
    setEntityProperties(newEntity3, 7, 3, "2020-07-11");
    datastore.put(newEntity3);

    Entity newEntity4 = new Entity(items.get(3));
    setEntityProperties(newEntity4, 8, 4, "2020-07-12");
    datastore.put(newEntity4);

    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("2020-07-05", "17.0");
    map.put("2020-07-12", "53.0");

    userInsights.updateUserStats(items);

    Assert.assertEquals(map, userInsights.aggregateUserData());
  }

  @Test
  public void createJson_returnsCorrectJsonString() {
    List<Key> items = createTestKeyList(0, 4);
    List<Item> itemProperties = new ArrayList<>();

    Entity newEntity = new Entity(items.get(0));
    setEntityProperties(newEntity, 5, 1, "2020-06-29");
    itemProperties.add(new Item(5.00, 1L, "2020-06-29"));
    datastore.put(newEntity);

    Entity newEntity2 = new Entity(items.get(1));
    setEntityProperties(newEntity2, 6, 2, "2020-06-30");
    itemProperties.add(new Item(6.00, 2L, "2020-06-30"));
    datastore.put(newEntity2);

    Entity newEntity3 = new Entity(items.get(2));
    setEntityProperties(newEntity3, 7, 3, "2020-07-11");
    itemProperties.add(new Item(7.00, 3L, "2020-07-11"));
    datastore.put(newEntity3);

    Entity newEntity4 = new Entity(items.get(3));
    setEntityProperties(newEntity4, 8, 4, "2020-07-12");
    itemProperties.add(new Item(8.00, 4L, "2020-07-12"));
    datastore.put(newEntity4);

    Map<String, String> map = new LinkedHashMap<String, String>();
    map.put("2020-07-05", "17.0");
    map.put("2020-07-12", "53.0");

    userInsights.updateUserStats(items);

    JsonObject testJson = new JsonObject();
    testJson.addProperty("weeklyAggregate", new Gson().toJson(map));
    testJson.addProperty("items", new Gson().toJson(itemProperties));
    String expectedJson = new Gson().toJson(testJson);

    Assert.assertEquals(expectedJson, userInsights.createJson());
  }

  @Test
  public void retreiveUserStats_withUnknownUser_returnsEmptyOptional() {
    // If a userStats object is not created with the given userId, an empty
    // Optional should be returned.
    userInsights = new UserInsights("unkownUserId");
    Optional<Entity> emptyOptional = Optional.empty();
    Assert.assertEquals(emptyOptional, userInsights.retreiveUserStats());
  }

  @Test
  public void aggregateUserData_withUnkownUser_returnsDefaultMap() {
    // If a userStats object is not created with the given userId, an empty
    // Optional should be returned.
    userInsights = new UserInsights("unkownUserId");
    Map<String, String> emptyMap = new LinkedHashMap<String, String>();
    emptyMap.put("weeklyAggregate", "");
    emptyMap.put("items", "");
    Assert.assertEquals(emptyMap, userInsights.aggregateUserData());
  }

  @Test
  public void createJson_withUnkownUser_returnsDefaultJson() {
    userInsights = new UserInsights("unknownUserId");
    JsonObject userJson = new JsonObject();
    userJson.addProperty("weeklyAggregate", "");
    userJson.addProperty("items", "");
    Assert.assertEquals(new Gson().toJson(userJson), userInsights.createJson());
  }

  /**
   * Creates a list of Item keys.
   *
   * @param start the number of the first element in the list
   * @param count the number of the elements after first element
   * @return an immutableList of keys with a number assigned to each of them "item0, item1... itemN"
   */
  private List<Key> createTestKeyList(int start, int count) {
    // System.out.println(ImmutableList.class);
    List<Key> keys = IntStream.range(start, start + count)
        .mapToObj(i -> KeyFactory.createKey("Item", "Item" + i))
        .collect(Collectors.toList());
    // Could not use toImmutableList().
    return ImmutableList.copyOf(keys);
  }

  /**
   * Assigns the parameters to the given entity.
   *
   * @param entity the entity to update
   * @param price price
   * @param quantity quantity
   * @param date date
   */
  private void setEntityProperties(Entity entity, double price, int quantity, String date) {
    entity.setProperty("price", price);
    entity.setProperty("quantity", quantity);
    entity.setProperty("date", date);
  }
}

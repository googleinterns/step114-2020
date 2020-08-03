package com.google.edith;

import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import org.mockito.MockitoAnnotations;

@RunWith(JUnit4.class)
public class QueryItemsTest {
  private QueryItems query;

  private final UserService userService = UserServiceFactory.getUserService();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  private Map<String, Object> map = ImmutableMap
            .of("com.google.appengine.api.users.UserService.user_id_key", "12345");

  private LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
        new LocalUserServiceTestConfig())
        .setEnvAttributes(map)
        .setEnvIsLoggedIn(true)
        .setEnvAuthDomain("gmail")
        .setEnvIsAdmin(true)
        .setEnvEmail("user@gmail.com");

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    query = new QueryItems(datastore, userService);
    createAndStoreEntites();
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Test
  public void findExpiredItems_expiredItem_displaysExpiredItem() {
    Assert.assertTrue(query.findExpiredItems().contains("Apple Juice"));
  }

  @Test
  public void findExpiredItems_notExpiredItem_doesntDisplayExpiredItem() {
    Assert.assertTrue(!query.findExpiredItems().contains("Bread"));
  }

  @Test
  public void findExpiredItems_expiredItemInWeeks_displaysExpiredItem() {
    Assert.assertTrue(query.findExpiredItems().contains("Peanut Butter"));
  }

  @Test
  public void findExpiredItems_duplicateItemsInReceipt_onlyDisplaysOne() {
    String json = query.findExpiredItems();
    JsonParser parser = new JsonParser();
    JsonArray inputJson = parser.parse(json).getAsJsonArray();
    Assert.assertTrue(inputJson.size() == 2);
  }

  // Creates and Stores Receipt and Item entities in Datastore.
  private void createAndStoreEntites() {
    Entity receipt1 = createReceiptEntity("12345", "Whole Foods", "2020-07-27", "weekend", "url1", 2.5f);
    Entity receipt2 = createReceiptEntity("12345", "Whole Foods", "2020-07-23", "sunday", "url2", 3.5f);
    Entity receipt3 = createReceiptEntity("12345", "Whole Foods", "2020-08-01", "sunday", "url2", 3.5f);
    createItemEntity(receipt1, "12345", "Apple Juice", 5, 5.6f, "fruit", "6.0 Days");
    createItemEntity(receipt1, "12345", "Peanut Butter", 6, 4.8f, "protein", "1.0 Weeks");
    createItemEntity(receipt2, "12345", "Peanut Butter", 5, 2.5f, "protein", "1.0 Weeks");
    createItemEntity(receipt3, "12345", "Bread", 3, 4.6f, "bread", "2.0 Weeks");
  }

  // Creates a Receipt Entity and stores it in Datastore.
  private Entity createReceiptEntity(String userId, String storeName, String date, String name, String fileUrl, float price) {
    Entity receiptEntity = new Entity("Receipt");
    receiptEntity.setProperty("userId", userId);
    receiptEntity.setProperty("storeName", storeName);
    receiptEntity.setProperty("date", date);
    receiptEntity.setProperty("name", name);
    receiptEntity.setProperty("fileUrl", fileUrl);
    receiptEntity.setProperty("price", price);
    datastore.put(receiptEntity);
    return receiptEntity;
  }

  // Creates an Item entity and stores it in Datastore.
  private void createItemEntity(Entity receipt, String userId, String name, int quantity, float price, String category, String date) {
    Entity itemEntity = new Entity("Item", receipt.getKey());
    itemEntity.setProperty("userId", userId);
    itemEntity.setProperty("name", name);
    itemEntity.setProperty("quantity", quantity);
    itemEntity.setProperty("price", price);
    itemEntity.setProperty("category", category);
    itemEntity.setProperty("date", date);
    datastore.put(itemEntity);
  }
}

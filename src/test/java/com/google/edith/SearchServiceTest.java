// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.edith;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableMap;
import com.google.edith.services.SearchService;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class SearchServiceTest {

  private SearchService searchService;
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
    searchService = new SearchService(datastore, userService);
    createAndStoreEntites();
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }
  
  // Filters entities of kind Receipt by userId and Name.
  @Test
  public void find_ReceiptWithGivenNameOnly_countEntities() {
    assertEquals(4, datastore.prepare(new Query("Receipt")).countEntities());
    List<Entity> foundEntities = searchService
            .findEntityFromDatastore("weekend", "", "Receipt", "", "");
    assertEquals(1, foundEntities.size());
  }

  // Filters entities of kind Receipt by userId and Date.
  @Test
  public void find_ReceiptWithGivenDateOnly_countEntities() {
    assertEquals(4, datastore.prepare(new Query("Receipt")).countEntities());
    List<Entity> foundEntities = searchService
            .findEntityFromDatastore("", "unknown", "Receipt", "", "");
    assertEquals(2, foundEntities.size());
  }

  // Filters entities of kind Receipt by userId, Date and Name.
  @Test
  public void find_ReceiptWithGivenNameAndDate_countEntities() {
    assertEquals(4, datastore.prepare(new Query("Receipt")).countEntities());
    // Filter by Name and Date.
    List<Entity> foundEntities = searchService
            .findEntityFromDatastore("weekend", "unknown", "Receipt", "", "");
    assertEquals(1, foundEntities.size());
  }

  // Filters entities of kind Item by userId and Name.
  @Test
  public void find_ItemWithGivenNameOnly_countEntities() {
    assertEquals(6, datastore.prepare(new Query("Item")).countEntities());
    // Only filter by name.
    List<Entity> foundEntities = searchService
            .findEntityFromDatastore("apple", "", "Item", "", "");
    assertEquals(3, foundEntities.size());
  }

  // Filters entities of kind Item by userId and Date.
  @Test
  public void find_ItemWithGivenDateOnly_countEntities() {
    assertEquals(6, datastore.prepare(new Query("Item")).countEntities());
    // Only filter by Date.
    List<Entity> foundEntities = searchService
            .findEntityFromDatastore("", "expire1", "Item", "", "");
    assertEquals(2, foundEntities.size());
    foundEntities = searchService
            .findEntityFromDatastore("", "expire4", "Item", "", "");
    assertEquals(1, foundEntities.size());
  }

  // Filters entities of kind Item by userId, Date and Name.
  @Test
  public void find_ItemWithGivenNameAndDate_countEntities() {
    assertEquals(6, datastore.prepare(new Query("Item")).countEntities());
    // Filter by Name and Date.
    List<Entity> foundEntities = searchService
            .findEntityFromDatastore("apple", "expire1", "Item", "", "");
    assertEquals(2, foundEntities.size());
    foundEntities = searchService
            .findEntityFromDatastore("apple", "expire2", "Item", "", "");
    assertEquals(1, foundEntities.size());
  }

  // Creates and Stores Receipt and Item entities in Datastore.
  private void createAndStoreEntites() {
    Entity receipt1 = createReceiptEntity("12345", "kro", "unknown", "weekend", "url1", 2.5f);
    Entity receipt2 = createReceiptEntity("12345", "wal", "known", "sunday", "url2", 3.5f);
    Entity receipt3 = createReceiptEntity("6789", "wal", "known", "sunday", "url2", 3.5f);
    Entity receipt4 = createReceiptEntity("12345", "whole", "unknown", "monday", "url3", 4.5f);
    createItemEntity(receipt1, "12345", "apple", 5, 2.5f, "fruit", "expire1");
    createItemEntity(receipt1, "12345", "apple", 6, 4.8f, "fruit", "expire1");
    createItemEntity(receipt1, "12345", "apple", 6, 4.8f, "fruit", "expire2");
    createItemEntity(receipt1, "12345", "berry", 10, 8.8f, "fruit", "expire4");
    createItemEntity(receipt2, "6789", "apple", 5, 2.5f, "fruit", "expire1");
    createItemEntity(receipt3, "6789", "banana", 3, 4.6f, "fruit", "expire4");
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

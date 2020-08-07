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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.common.collect.ImmutableMap;
import com.google.edith.services.StoreReceiptService;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class StoreReceiptServiceTest {
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private Map<String, Object> userIdMap =
      ImmutableMap.of("com.google.appengine.api.users.UserService.user_id_key", "12345");

  private LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(
              new LocalUserServiceTestConfig(), new LocalDatastoreServiceTestConfig())
          .setEnvAttributes(userIdMap)
          .setEnvIsLoggedIn(true)
          .setEnvAuthDomain("gmail")
          .setEnvIsAdmin(true)
          .setEnvEmail("user@gmail.com");

  private StoreReceiptService storeReceiptService = new StoreReceiptService(datastore);

  @Mock HttpServletRequest request;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Test
  // Tests if helper function is working correctly.
  public void createUserInfoEntity_CreatesEntity() {
    assertEquals(0, datastore.prepare(new Query("UserInfo")).countEntities());
    Entity userInfoEntity = createUserInfoEntity();
    assertEquals(1, datastore.prepare(new Query("UserInfo")).countEntities());
  }

  @Test
  // Tests if Receipt entity and Item entities are stored.
  public void storeEntites_EntitiesAreStoredInDatastore() {
    Entity userInfoEntity = createUserInfoEntity();
    Receipt testingReceipt = createEntities();

    storeReceiptService.storeEntites(testingReceipt);

    assertEquals(1, datastore.prepare(new Query("Receipt")).countEntities());
    assertEquals(2, datastore.prepare(new Query("Item")).countEntities());
  }

  @Test
  // Check the JSON string is transformed into Receipt object.
  public void parseReceiptFromForm_CreatesReceiptObjects() throws IOException {
    // TODO(prashantneu@) add better test with correct testJson string.
    String testJson =
        "{\"data\":\"{\\\"userId\\\":\\\"23\\\",\\\"storeName\\\":\\\"kro\\\",\\\"date\\\":\\\"date\\\",\\\"name\\\":\\\"exp\\\",\\\"fileUrl\\\":\\\"url\\\",\\\"totalPrice\\\":0.5,\\\"items\\\":[{\\\"userId\\\":\\\"23\\\",\\\"name\\\":\\\"kro\\\",\\\"price\\\":0.5,\\\"quantity\\\":2,\\\"category\\\":\\\"cat\\\",\\\"expireDate\\\":\\\"date\\\"}]}\"}";
    Reader inputString = new StringReader(testJson);
    BufferedReader reader = new BufferedReader(inputString);
    when(request.getReader()).thenReturn(reader);

    Receipt parsedReceipt = storeReceiptService.parseReceiptFromForm(reader);
    assertNotNull(parsedReceipt);
  }

  @Test
  public void storeEntites_storesEntitesWithCorrectAncestorForReceipt() {
    Entity userInfoEntity = createUserInfoEntity();
    Receipt testingReceipt = createEntities();

    storeReceiptService.storeEntites(testingReceipt);

    Entity receiptEntity = datastore.prepare(new Query("Receipt")).asSingleEntity();
    assertTrue(receiptEntity.getKey().toString().contains("UserInfo"));
  }

  @Test
  public void storeEntites_storesEntitesWithCorrectAncestorForItem() {
    Entity userInfoEntity = createUserInfoEntity();
    Receipt testingReceipt = createEntities();
    Query query =
        new Query("Item")
            .setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, "apple"));

    storeReceiptService.storeEntites(testingReceipt);

    Entity item1 = datastore.prepare(query).asSingleEntity();
    // Item entity must contain UserInfo and Receipt both in the key.
    assertTrue(item1.getKey().toString().contains("UserInfo"));
    assertTrue(item1.getKey().toString().contains("Receipt"));
  }

  // Helper method to create entity of type UserInfo.
  private Entity createUserInfoEntity() {
    Entity userInfoEntity = new Entity("UserInfo");
    userInfoEntity.setProperty("id", "23");
    userInfoEntity.setProperty("firstName", "firstName");
    userInfoEntity.setProperty("lastName", "lastName");
    userInfoEntity.setProperty("userName", "userName");
    userInfoEntity.setProperty("favoriteStore", "favoriteStore");
    datastore.put(userInfoEntity);
    return userInfoEntity;
  }

  // Helper method to create Receipt entity with items having two
  // Item objects.
  private Receipt createEntities() {
    Item item1 =
        Item.builder()
            .setUserId("23")
            .setName("apple")
            .setPrice((float) 1.5)
            .setQuantity(2)
            .setDate("yy")
            .setCategory("fruit")
            .setExpiration("date")
            .build();

    Item item2 =
        Item.builder()
            .setUserId("23")
            .setName("ball")
            .setPrice((float) 2.5)
            .setQuantity(1)
            .setDate("mm")
            .setCategory("notfruit")
            .setExpiration("date")
            .build();
    Item[] items = {item1, item2};
    Receipt testingReceipt = new Receipt("23", "kro", "date", "exp", "url", 0.5f, items);
    return testingReceipt;
  }
}

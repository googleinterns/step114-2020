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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.servlets.Receipt;
import com.google.edith.servlets.Item;
import com.google.edith.services.StoreReceiptService;
import com.google.edith.services.UserInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class StoreReceiptServiceTest {
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private Map<String, Object> map = new HashMap<String, Object>() {{
        put("com.google.appengine.api.users.UserService.user_id_key", "12345");
    }};
    
  private LocalServiceTestHelper testHelper = 
          new LocalServiceTestHelper(
                new LocalUserServiceTestConfig(),
                new LocalDatastoreServiceTestConfig())
          .setEnvAttributes(map)
          .setEnvIsLoggedIn(true)
          .setEnvAuthDomain("gmail")
          .setEnvIsAdmin(true)
          .setEnvEmail("user@gmail.com");
  
  private StoreReceiptService storeReceiptService;

  @Mock
  HttpServletRequest request;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    storeReceiptService = new StoreReceiptService(datastore);
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Test
  // Tests if Receipt entity and Item entities are stored.
  public void testStoreEntities() {
    assertEquals(0, datastore.prepare(new Query("UserInfo")).countEntities());
    Entity userInfoEntity = createUserInfoEntity();
    assertEquals(1, datastore.prepare(new Query("UserInfo")).countEntities());
    Receipt testingReceipt = createEntities();
    assertEquals(0, datastore.prepare(new Query("Receipt")).countEntities());
    storeReceiptService.storeEntites(testingReceipt);
    assertEquals(1, datastore.prepare(new Query("Receipt")).countEntities());
    assertEquals(2, datastore.prepare(new Query("Item")).countEntities());
  }

  @Test
  // Check the JSON string is transformed into Receipt object.
  public void testParseReceiptFromForm() throws IOException {
    String testJson = "{\"data\":\"{\\\"userId\\\":\\\"23\\\",\\\"storeName\\\":\\\"kro\\\",\\\"date\\\":\\\"date\\\",\\\"name\\\":\\\"exp\\\",\\\"fileUrl\\\":\\\"url\\\",\\\"totalPrice\\\":0.5,\\\"items\\\":[{\\\"userId\\\":\\\"23\\\",\\\"name\\\":\\\"kro\\\",\\\"price\\\":0.5,\\\"quantity\\\":2,\\\"category\\\":\\\"cat\\\",\\\"expireDate\\\":\\\"date\\\"}]}\"}";
    Reader inputString = new StringReader(testJson);
    BufferedReader reader = new BufferedReader(inputString);
    when(request.getReader()).thenReturn(reader);
    Receipt parsedReceipt = storeReceiptService.parseReceiptFromForm(request);
    assertNotNull(parsedReceipt);
    assertTrue(parsedReceipt instanceof Receipt);
  }
  
  @Test
  public void testReceiptParent() {
    Entity userInfoEntity = createUserInfoEntity();
    assertEquals(0, datastore.prepare(new Query("Receipt")).countEntities());
    Receipt testingReceipt = createEntities();
    storeReceiptService.storeEntites(testingReceipt);
    Entity receiptEntity = datastore.prepare(new Query("Receipt")).asSingleEntity();
    assertTrue(receiptEntity.getKey().toString().contains("UserInfo"));
  }

  @Test
  public void testItemParent() {
    Entity userInfoEntity = createUserInfoEntity();
    assertEquals(0, datastore.prepare(new Query("Receipt")).countEntities());
    Receipt testingReceipt = createEntities();
    storeReceiptService.storeEntites(testingReceipt);
    Query query = new Query("Item")
            .setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, "apple"));
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
    Item item1 = new Item("23", "apple", 1.5f, 2, "fruit", "date");
    Item item2 = new Item("23", "ball", 2.5f, 1, "notfruit", "date");
    Item[] items = {item1, item2};
    Receipt testingReceipt = new Receipt("23","kro", "date", "exp", "url", 0.5f, items);
    return testingReceipt;
  }
}

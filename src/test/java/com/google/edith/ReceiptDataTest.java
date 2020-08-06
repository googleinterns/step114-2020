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
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.edith.interfaces.ExtractReceiptInterface;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import com.google.edith.servlets.ReceiptData;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class ReceiptDataTest {
  private Map<String, Object> map =
      ImmutableMap.of("com.google.appengine.api.users.UserService.user_id_key", "12345");

  private final LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvAttributes(map)
          .setEnvIsLoggedIn(true)
          .setEnvAuthDomain("gmail")
          .setEnvIsAdmin(true)
          .setEnvEmail("user@gmail.com");

  @Mock ExtractReceiptInterface extractReceiptImplementation;
  private final UserService userService = UserServiceFactory.getUserService();

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    receiptData = new ReceiptData(extractReceiptImplementation);
    testHelper.setUp();
    setUpItemDescription();
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  private ReceiptData receiptData;

  @Test
  public void extractReceiptData_hasRightNumberOfItems() throws IOException {
    when(extractReceiptImplementation.getCurrentLoggedInUser()).thenReturn(userService.getCurrentUser());
    Receipt extractedReceipt = receiptData.extractReceiptData("someBlobKey", "expense");
    Item[] items = extractedReceipt.getItems();
    assertEquals(2, items.length);
  }

  @Test
  public void extractReceiptData_hasRightCorrectExpenseName() throws IOException {
    when(extractReceiptImplementation.getCurrentLoggedInUser()).thenReturn(userService.getCurrentUser());
    Receipt extractedReceipt = receiptData.extractReceiptData("someBlobKey", "expense");
    assertEquals("expense", extractedReceipt.getName());
  }

  private void setUpItemDescription() throws IOException {
    ImmutableMap<String, String> item1 = ImmutableMap.of("itemName", "apple", "itemPrice", "2.5");
    ImmutableMap<String, String> item2 = ImmutableMap.of("itemName", "ball", "itemPrice", "4.5");
    ImmutableList<ImmutableMap<String, String>> itemsDescription = ImmutableList.of(item1, item2);
    when(extractReceiptImplementation.extractReceipt("someBlobKey")).thenReturn(itemsDescription);
  }
}

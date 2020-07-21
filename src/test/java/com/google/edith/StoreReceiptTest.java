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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.services.StoreReceiptService;
import com.google.edith.servlets.StoreReceipt;
import com.google.edith.servlets.Receipt;
import com.google.edith.servlets.Item;
import org.mockito.MockitoAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoreReceiptTest {

  private final LocalServiceTestHelper testHelper = 
      new LocalServiceTestHelper(
        new LocalDatastoreServiceTestConfig());

  private StoreReceipt storeReceipt;

  @Mock
  StoreReceiptService storeReceiptService;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    storeReceipt = new StoreReceipt(storeReceiptService);
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Test
  public void testServiceMethodsAreCalled() throws IOException {
    Item item1 = new Item("23", "apple", 1.5f, 2, "fruit", "date");
    Item[] items = {item1};
    Receipt receipt = new Receipt("23","kro", "date", "exp", "url", 0.5f, items);
    when(storeReceiptService.parseReceiptFromForm(request)).thenReturn(receipt);

    storeReceipt.doPost(request, response);
    verify(storeReceiptService, times(1)).parseReceiptFromForm(request);
    verify(storeReceiptService, times(1)).storeEntites(receipt);
    verify(response, times(1)).sendRedirect("/");
  }
}
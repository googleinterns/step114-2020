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

package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.edith.services.StoreReceiptService;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to store different entities in Datastore. */
@WebServlet("/store-receipt")
public class StoreReceipt extends HttpServlet {
  private StoreReceiptService storeReceiptService;

  public StoreReceipt() {
    this.storeReceiptService =
        new StoreReceiptService(DatastoreServiceFactory.getDatastoreService());
  }

  public StoreReceipt(StoreReceiptService storeReceiptService) {
    this.storeReceiptService = storeReceiptService;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Receipt receipt = storeReceiptService.parseReceiptFromForm(request);
    storeReceiptService.storeEntites(receipt);
    response.sendRedirect("/");
  }
}

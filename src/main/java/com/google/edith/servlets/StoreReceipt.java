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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to store different entities in Datastore.
 */
@WebServlet("/store-receipt")
public class StoreReceipt extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BufferedReader bufferedReader = request.getReader();

    Gson gson = new Gson();
    JsonParser parser = new JsonParser();
    JsonObject json = (JsonObject) parser.parse(bufferedReader);
    String receiptJsonString = json.get("data").getAsString();
    
    Receipt receipt = gson.fromJson(receiptJsonString, Receipt.class);

    String userId = receipt.getUserId();
    String storeName = receipt.getStoreName();
    String date = receipt.getDate();
    String name = receipt.getName();
    String fileUrl = receipt.getFileUrl();
    float totalPrice = receipt.getTotalPrice();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity receiptEntity = new Entity("Receipt");
    receiptEntity.setProperty("userId", userId);
    receiptEntity.setProperty("storeName", storeName);
    receiptEntity.setProperty("date", date);
    receiptEntity.setProperty("name", name);
    receiptEntity.setProperty("fileUrl", fileUrl);
    receiptEntity.setProperty("totalPrice", totalPrice);
    datastore.put(receiptEntity);
    // System.out.println(receipt);
    Item[] items = receipt.getItems();
    for (Item item: items) {
      String itemName = item.getName();
      float price = item.getPrice();
      int quantity = item.getQuantity();
      String category = item.getCategory();
      String expireDate = item.getExpireDate();
  
      Entity itemEntity = new Entity("Item");
      itemEntity.setProperty("userId", userId);
      itemEntity.setProperty("name", itemName);
      itemEntity.setProperty("quantity", quantity);
      itemEntity.setProperty("category", category);
      itemEntity.setProperty("expireDate", expireDate);
      datastore.put(itemEntity);

    }
  }
}

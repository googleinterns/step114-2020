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

package com.google.edith.services;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.edith.interfaces.StoreReceiptInterface;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public final class StoreReceiptService implements StoreReceiptInterface {

  private final DatastoreService datastore;

  public StoreReceiptService(DatastoreService datastore) {
    this.datastore = datastore;
  }

  @Override
  public void storeEntites(Receipt receipt) {
    storeReceiptEntity(receipt);
  }

  @Override
  public Receipt parseReceiptFromForm(HttpServletRequest request) throws IOException {
    BufferedReader bufferedReader = request.getReader();
    Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemDeserializer()).create();
    JsonParser parser = new JsonParser();
    JsonObject json = (JsonObject) parser.parse(bufferedReader);
    return gson.fromJson(json, Receipt.class);
  }

  /**
   * Receives Receipt object and creates entity of type Receipt and stores it in Datastore.
   *
   * @param receipt - object which holds info of parsed file.
   */
  private void storeReceiptEntity(Receipt receipt) {
    String userId = receipt.getUserId();
    String storeName = receipt.getStoreName();
    String date = receipt.getDate();
    String name = receipt.getName();
    String fileUrl = receipt.getFileUrl();
    float totalPrice = receipt.getTotalPrice();

    Optional<Entity> optEntity = getUserInfoEntity(userId);
    Entity userInfoEntity = optEntity.get();
    Entity receiptEntity = new Entity("Receipt", userInfoEntity.getKey());
    receiptEntity.setProperty("userId", userId);
    receiptEntity.setProperty("storeName", storeName);
    receiptEntity.setProperty("date", date);
    receiptEntity.setProperty("name", name);
    receiptEntity.setProperty("fileUrl", fileUrl);
    receiptEntity.setProperty("totalPrice", totalPrice);
    datastore.put(receiptEntity);
    storeReceiptItemsEntity(receipt, receiptEntity);
  }

  /**
   * Stores parsed item from the form with receiptEntity as a parent in the datastore.
   *
   * @param items - request which contains the form body.
   * @param receiptEntity - request which contains the form body.
   */
  private void storeReceiptItemsEntity(Receipt receipt, Entity receiptEntity) {
    List<Item> items = Arrays.asList(receipt.getItems());
    for (Item item : items) {
      String userId = item.userId();
      String itemName = item.name();
      double price = item.price();
      long quantity = item.quantity();
      String category = item.category();
      String expireDate = item.expiration();

      Entity itemEntity = new Entity("Item", receiptEntity.getKey());
      itemEntity.setProperty("userId", userId);
      itemEntity.setProperty("name", itemName);
      itemEntity.setProperty("quantity", quantity);
      itemEntity.setProperty("category", category);
      itemEntity.setProperty("expireDate", expireDate);
      datastore.put(itemEntity);
    }
  }

  /**
   * Returns the UserInfo entity with user id. Given id is not of UserInfo kind but a field of that
   * kind.
   *
   * @param id - id of the user who is logged in.
   */
  private Optional<Entity> getUserInfoEntity(String id) {

    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    return Optional.ofNullable(results.asSingleEntity());
  }

  /**
   * Custom Deserializer to deserialize Item class as it is an abstract class.
   */
  private class ItemDeserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      JsonObject jsonObj = json.getAsJsonObject();
      String userId = jsonObj.get("userId").getAsString();
      String category = jsonObj.get("category").getAsString();
      String name = jsonObj.get("name").getAsString();
      double price = jsonObj.get("price").getAsDouble();
      long quantity = jsonObj.get("quantity").getAsLong();
      String date = jsonObj.get("date").getAsString();
      String expiration = jsonObj.get("expiration").getAsString();
      Item item =
          Item.builder()
              .setUserId(userId)
              .setName(name)
              .setPrice(price)
              .setQuantity(quantity)
              .setDate(date)
              .setCategory(category)
              .setExpiration(expiration)
              .build();
      return item;
    }
  }
}

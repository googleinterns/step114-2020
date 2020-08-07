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
  public Receipt parseReceiptFromForm(BufferedReader bufferedReader) throws IOException {
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

    Optional<Entity> optEntity = getUserInfoEntity(receipt.getUserId());
    Entity userInfoEntity = optEntity.get();
    Entity receiptEntity = new Entity("Receipt", userInfoEntity.getKey());
    receiptEntity.setProperty("userId", receipt.getUserId());
    receiptEntity.setProperty("storeName", receipt.getStoreName());
    receiptEntity.setProperty("date", receipt.getDate());
    receiptEntity.setProperty("name", receipt.getName());
    receiptEntity.setProperty("fileUrl", receipt.getFileUrl());
    receiptEntity.setProperty("price", receipt.getTotalPrice());
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
      Entity itemEntity = new Entity("Item", receiptEntity.getKey());
      itemEntity.setProperty("userId", item.userId());
      itemEntity.setProperty("name", item.name());
      itemEntity.setProperty("quantity", item.quantity());
      itemEntity.setProperty("price", item.price());
      itemEntity.setProperty("category", item.category());
      itemEntity.setProperty("expireDate", item.expiration());
      itemEntity.setProperty("date", item.date());
      datastore.put(itemEntity);
    }
  }

  /**
   * Returns the UserInfo entity with user id. Given id is not of UserInfo kind but a field of that
   * kind.
   */
  private Optional<Entity> getUserInfoEntity(String id) {

    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    return Optional.ofNullable(results.asSingleEntity());
  }

  /** Custom Deserializer to deserialize Item class as it is an abstract class. */
  private class ItemDeserializer implements JsonDeserializer<Item> {
    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      JsonObject jsonObj = json.getAsJsonObject();
      Item item =
          Item.builder()
              .setUserId(jsonObj.get("userId").getAsString())
              .setName(jsonObj.get("name").getAsString())
              .setPrice(jsonObj.get("price").getAsDouble())
              .setQuantity(jsonObj.get("quantity").getAsLong())
              .setDate(jsonObj.get("date").getAsString())
              .setCategory(jsonObj.get("category").getAsString())
              .setExpiration(jsonObj.get("expiration").getAsString())
              .build();
      return item;
    }
  }
}

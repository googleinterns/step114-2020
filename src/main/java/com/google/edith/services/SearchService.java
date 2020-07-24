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
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SearchService extends HttpServlet {

  private final DatastoreService datastore;

  public SearchService(DatastoreService datastore) {
    this.datastore = datastore;
  }

  /**
   * Creates an array of Receipt objects from entites of
   * kind Receipt found in the datastore.
   * @param entities - entities of kind Receipt found in datastore.
   * @return Receipt[] - array of Receipt objects 
   */
  public Receipt[] createReceiptObjects(List<Entity> entities) {
    List<Receipt> receipts = new ArrayList<>();

    for (Entity entity: entities) {
      Key entityKey = entity.getKey();
      Query itemQuery = new Query("Item", entityKey);
      PreparedQuery results = datastore.prepare(itemQuery);
      List<Entity> itemEntities = results.asList(FetchOptions.Builder.withLimit(Integer.MAX_VALUE));
      Item[] items = createItemObjects(itemEntities);
      String userId = (String) entity.getProperty("userId");
      String storeName = (String) entity.getProperty("storeName");
      String date = (String) entity.getProperty("date");
      String name = (String) entity.getProperty("name");
      String fileUrl = (String) entity.getProperty("fileUrl");
      float totalPrice = (float) ((double) entity.getProperty("price"));
      Receipt receipt = new Receipt(userId, storeName, date, name, fileUrl, totalPrice, items);
      receipts.add(receipt);
    }

    return receipts.toArray(new Receipt[0]);
  }

  /**
   * Creates an array of Item objects from entites of
   * kind Item found in the datastore.
   * @param entities - entities of kind Item found in datastore.
   * @return Item[] - array of Item objects.
   */
  public Item[] createItemObjects(List<Entity> entities) {
    List<Item> itemsList = new ArrayList<>();

    for (Entity entity: entities) {
      String userId = (String) entity.getProperty("userId");
      String itemName = (String) entity.getProperty("name");
      float price = (float) ((double) entity.getProperty("price"));
      int quantity = (int ) ((long) entity.getProperty("quantity"));
      String category = (String) entity.getProperty("category");
      String expireDate = (String) entity.getProperty("date");

      Item receiptItem = new Item(userId, itemName, price, quantity, category, expireDate);
      itemsList.add(receiptItem);
    }

    return itemsList.toArray(new Item[0]);
  }

  /**
   * Creates a list of entites found from given name, date
   * kind and sorts on given order on given proprty
   * @param name -name property of the entity.
   * @param date - date property of the entity.
   * @param kind - kind of the entity stored in datastore.
   * @param sortOrder - order to sort the entities.
   * @param sortOnProperty - property on which to sort the order.
   * @return List<Entity> - list of entites found from the query.
   */
  public List<Entity> findEntityFromDatastore(String name, String date, String kind, String sortOrder, String sortOnProperty) {
    Query query = prepareQuery(name, date, kind, sortOrder, sortOnProperty);
    PreparedQuery results = datastore.prepare(query);
    List<Entity> entities = results.asList(FetchOptions.Builder
                .withLimit(Integer.MAX_VALUE));
    return entities;
  }

  /**
   * Prepares a query for the search on datastore.
   * @param name -name property of the entity.
   * @param date - date property of the entity.
   * @param kind - kind of the entity stored in datastore.
   * @param sortOrder - order to sort the entities.
   * @param sortOnProperty - property on which to sort the order.
   * @return Query - query to be made on the datastore
   */
  private Query prepareQuery(String name, String date, String kind, String sortOrder, String sortOnProperty) {
    Query query;
    Filter entityFilter = prepareFilter(name, date);

    if (kind.equals("Receipt")) {
      query = new Query("Receipt").setFilter(entityFilter);
    } else {
      query = new Query("Item").setFilter(entityFilter);
    }
    
    if (sortOrder.equals("Ascending")) query = query
          .addSort(sortOnProperty, SortDirection.ASCENDING);
    if (sortOrder.equals("Descending")) query = query
          .addSort(sortOnProperty, SortDirection.DESCENDING);

    return query;
  }

  /**
   * Creates a filter either on name and date.
   * @param name -name property of the entity.
   * @param date - date property of the entity.
   * @return Filter -filter options to filer on entites.
   */
  private Filter prepareFilter(String name, String date) {
    UserService userService = UserServiceFactory.getUserService();
    String loggedInUserId = userService.getCurrentUser().getUserId();

    List<Filter> filters = new ArrayList<>();
    filters.add(new FilterPredicate("userId", FilterOperator.EQUAL, loggedInUserId));
    
    if (!name.isEmpty()) {
      filters.add(new FilterPredicate("name", FilterOperator.EQUAL, name));
    }

    if (!date.isEmpty()) {
      filters.add(new FilterPredicate("date", FilterOperator.EQUAL, date));
    }

    return new CompositeFilter(CompositeFilterOperator.AND, filters);
  }
}

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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.common.collect.ImmutableList;
import com.google.edith.interfaces.SearchService;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import java.util.ArrayList;
import java.util.List;

/** Service to create query and handle searching on entities. */
public class SearchServiceImpl implements SearchService {

  private final DatastoreService datastore;
  private UserService userService;

  public SearchServiceImpl(DatastoreService datastore, UserService userService) {
    this.datastore = datastore;
    this.userService = userService;
  }

  @Override
  public ImmutableList<Receipt> createReceiptObjects(ImmutableList<Entity> entities) {
    List<Receipt> receipts = new ArrayList<>();

    for (Entity entity : entities) {
      Key entityKey = entity.getKey();
      Query itemQuery = new Query("Item", entityKey);
      PreparedQuery results = datastore.prepare(itemQuery);
      List<Entity> itemEntities = results.asList(FetchOptions.Builder.withLimit(Integer.MAX_VALUE));
      ImmutableList<Item> items = createItemObjects(ImmutableList.copyOf(itemEntities));
      // Datastore returns as double even when uploaded as float.
      Receipt receipt =
          new Receipt(
              (String) entity.getProperty("userId"),
              (String) entity.getProperty("storeName"),
              (String) entity.getProperty("date"),
              (String) entity.getProperty("name"),
              (String) entity.getProperty("fileUrl"),
              // Datastore returns as double even when uploaded as float
              (float) ((double) entity.getProperty("price")),
              items.toArray(new Item[items.size()]));

      receipts.add(receipt);
    }
    return ImmutableList.copyOf(receipts);
  }

  @Override
  public ImmutableList<Item> createItemObjects(ImmutableList<Entity> entities) {
    List<Item> itemsList = new ArrayList<>();

    for (Entity entity : entities) {
      Item item =
          Item.builder()
              .setUserId((String) entity.getProperty("userId"))
              .setName((String) entity.getProperty("name"))
              .setPrice((double) entity.getProperty("price"))
              .setQuantity((long) entity.getProperty("quantity"))
              .setDate((String) entity.getProperty("date"))
              .setCategory((String) entity.getProperty("category"))
              .setExpiration((String) entity.getProperty("expireDate"))
              .build();

      itemsList.add(item);
    }
    return ImmutableList.copyOf(itemsList);
  }

  @Override
  public ImmutableList<Entity> findEntityFromDatastore(
      String name, String date, String kind, String sortOrder, String sortOnProperty) {
    Query query = prepareQuery(name, date, kind, sortOrder, sortOnProperty);
    PreparedQuery results = datastore.prepare(query);
    List<Entity> entities = results.asList(FetchOptions.Builder.withLimit(Integer.MAX_VALUE));
    return ImmutableList.copyOf(entities);
  }

  /**
   * Prepares a query for the search on datastore.
   *
   * @param name -name property of the entity.
   * @param date - date property of the entity.
   * @param kind - kind of the entity stored in datastore.
   * @param sortOrder - order to sort the entities.
   * @param sortOnProperty - property on which to sort the order.
   * @return Query - query to be made on the datastore
   */
  private Query prepareQuery(
      String name, String date, String kind, String sortOrder, String sortOnProperty) {
    Query query;
    Filter entityFilter = prepareFilter(name, date);

    if (kind.equals("Receipt")) {
      query = new Query("Receipt").setFilter(entityFilter);
    } else {
      query = new Query("Item").setFilter(entityFilter);
    }

    if (sortOrder.equals("Ascending"))
      query = query.addSort(sortOnProperty, SortDirection.ASCENDING);
    if (sortOrder.equals("Descending"))
      query = query.addSort(sortOnProperty, SortDirection.DESCENDING);
    return query;
  }

  /**
   * Creates a filter either on name and date.
   *
   * @param name -name property of the entity.
   * @param date - date property of the entity.
   * @return Filter -filter options to filer on entites.
   */
  private Filter prepareFilter(String name, String date) {
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

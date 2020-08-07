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

package com.google.edith.interfaces;

import com.google.appengine.api.datastore.Entity;
import com.google.common.collect.ImmutableList;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;

/** Operations for searching entities in Datastore with given filters. */
public interface SearchService {
  /**
   * Creates an ImmutableList of Receipt objects from receipt entites.
   *
   * @param entities - entities of kind Receipt found in datastore.
   * @return ImmutableList<Receipt> - ImmutableList of Receipt objects
   */
  ImmutableList<Receipt> createReceiptObjects(ImmutableList<Entity> entities);

  /**
   * Creates an ImmutableList of Item objects from item entites.
   *
   * @param entities - entities of kind Item found in datastore.
   * @return ImmutableList<Item> - ImmutableList of Item objects.
   */
  ImmutableList<Item> createItemObjects(ImmutableList<Entity> entities);

  /**
   * Creates an ImmutableList of entites found from given name, date, kind and sorts on given order
   * on given property.
   *
   * @param name -name property of the entity.
   * @param date - date property of the entity.
   * @param kind - kind of the entity stored in datastore.
   * @param sortOrder - order to sort the entities.
   * @param sortOnProperty - property on which to sort the order.
   * @return ImmutableList<Entity> - ImmutableList of entites found from the query.
   */
  ImmutableList<Entity> findEntityFromDatastore(
      String name, String date, String kind, String sortOrder, String sortOnProperty);
}

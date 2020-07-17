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

import com.google.auto.value.AutoValue;

/** Encapsulate User info and logout url. */

public class Item {
  public String userId;
  public String name;
  public float price;
  public int quantity;
  public String category;
  public String expireDate;

  public Item(String userId, String name, float price, int quantity, String category, String expireDate) {
    this.userId = userId;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.category = category;
    this.expireDate = expireDate;
  }

  public String getUserID() {
    return this.userId;
  }

  public String getName() {
    return this.name;
  }

  public float getPrice() {
    return this.price;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public String getCategory() {
    return this.category;
  }

  public String getExpireDate() {
    return this.expireDate;
  }

  public String toString() {
    return (this.userId + " " + this.name + " " + this.price + " " + this.quantity + " " + this.category);
  }

}
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

import com.google.appengine.api.users.User;

/** Encapsulate User info and logout url. */
public final class Item {
  private final String userId;
  private final String name;
  private final float price;
  private final int quantity;
  private final String category;

  Item(String userId, String name, float price) {
    this.userId = userId;
    this.name = name;
    this.price = price;
    this.quantity = 0;
    this.category = "unknown";
  }

  public String toString() {
    return (this.userId + " " + this.name + " " + this.price + " " + this.quantity + " " + this.category);
  }
}

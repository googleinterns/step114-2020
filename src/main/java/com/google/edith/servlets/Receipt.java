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
// TODO(@prashantneu) Use Auto_Value for this class.
/** Encapsulate User info and logout url. */
public final class Receipt {
  private final String userId;
  private final String storeName;
  private final String date;
  private final String name;
  private final String fileUrl;
  private final float totalPrice;
  private final Item[] items;

  public Receipt(
      String userId,
      String storeName,
      String date,
      String name,
      String fileUrl,
      float totalPrice,
      Item[] items) {
    this.userId = userId;
    this.storeName = storeName;
    this.date = date;
    this.name = name;
    this.fileUrl = fileUrl;
    this.totalPrice = totalPrice;
    this.items = items;
  }

  public String getName() {
    return this.name;
  }

  public Item[] getItems() {
    return this.items;
  }
}

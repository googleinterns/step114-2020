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

 /** Used to hold the properties of an item Entity in datastore. */
@AutoValue
public abstract class Item {
  public abstract String name();
  public abstract String userId();
  public abstract String category();
  public abstract double price(); 
  public abstract long quantity();  
  public abstract String date(); 
  public abstract String receiptId();
  public abstract String expireDate();

  public static Builder builder() {
    return new AutoValue_Item.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setName(String value);
    public abstract Builder setUserId(String value);
    public abstract Builder setCategory(String value);
    public abstract Builder setPrice(double value); 
    public abstract Builder setQuantity(long value);  
    public abstract Builder setDate(String value); 
    public abstract Builder setReceiptId(String value);
    public abstract Builder setExpireDate(String value);
    public abstract Item build();
  }
}
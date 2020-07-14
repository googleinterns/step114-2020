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
@AutoValue
abstract class Item {
  abstract String userId();
  abstract String name();
  abstract float price();
  abstract int quantity();
  abstract String category();
  abstract String expireDate();
  
  static Builder builder() {
    return new AutoValue_Item.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setUserId(String value);
    abstract Builder setName(String value);
    abstract Builder setPrice(float value);
    abstract Builder setQuantity(int value);
    abstract Builder setCategory(String value);
    abstract Builder setExpireDate(String value);
    abstract Item build();
  }
}

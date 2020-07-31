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

import com.google.auto.value.AutoValue;

/** Encapsulate User info and logout url. */
@AutoValue
abstract class UserInfo {

  abstract String firstName();
  abstract String lastName();
  abstract String userName();
  abstract String favoriteStore();
  abstract String email();

  abstract String userId();

  abstract String logOutUrl();

  static Builder builder() {
    return new AutoValue_UserInfo.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setFirstName(String value);
    abstract Builder setLastName(String value);
    abstract Builder setUserName(String value);
    abstract Builder setFavoriteStore(String value);
    abstract Builder setEmail(String value);

    abstract Builder setUserId(String value);

    abstract Builder setLogOutUrl(String value);

    abstract UserInfo build();

  }
}

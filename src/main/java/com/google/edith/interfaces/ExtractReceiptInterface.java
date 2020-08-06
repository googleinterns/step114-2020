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

import com.google.appengine.api.users.User;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;

/** Operations for handling parsing receipt using Document AI API. * */
public interface ExtractReceiptInterface {

  /**
   * Creates a list of maps of item descriptions.
   *
   * @return Lis<Map<String, String>> - a list of maps where item name is key and item price is
   *     value
   */
  public ImmutableList<ImmutableMap<String, String>> extractReceipt(String blobKey)
      throws IOException;

  /**
   * Gets the user who is currently logged in.
   *
   * @return User - user object which contains the information of logged in user.
   */
  public User getCurrentLoggedInUser();
}

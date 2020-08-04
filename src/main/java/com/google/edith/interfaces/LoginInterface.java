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

import javax.servlet.http.HttpServletRequest;

/** Operations for handling user authentication. */
public interface LoginInterface {
  /**
   * Converts UserInfo object to JSON string.
   *
   * @return String - JSON string.
   */
  String createJsonFromUserInfo();

  /**
   * Checks if a user is logged in.
   *
   * @return boolean - true if a user is logged in.
   */
  boolean checkUserLoggedIn();

  /**
   * Creates a url of a user to log in.
   *
   * @param destinationUrl - url to return when after visiting login portal.
   * @return String - url which redirects to login portal.
   */
  String createLoginUrl(String destinationUrl);

  /**
   * Stores the userInfo entity in the datastore.
   *
   * @param request - request from the UserInfoModalBox component.
   */
  void storeUserInfoEntityInDatastore(HttpServletRequest request);
}

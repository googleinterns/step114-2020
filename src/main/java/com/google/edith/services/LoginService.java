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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Service that handles user accounts and keeps user information like user name and favortie
 * store in datastore.
 */
public final class LoginService {
  private final UserService userService;
  private final DatastoreService datastore;

  public LoginService(UserService userService, DatastoreService datastore) {
    this.userService = userService;
    this.datastore = datastore;
  }

  /**
  * Converts UserInfo object to JSON string.
  * @return String - JSON string.
  */
  public String createJsonOfUserInfo() {
    Gson gson = new Gson();
    UserInfo loggedInUser = createUserInfo();
    return gson.toJson(loggedInUser);
  }

  /**
  * Checks if a user is logged in.
  * @return boolean - true if a user is logged in.
  */
  public boolean checkUserLoggedIn() {
    return userService.isUserLoggedIn();
  }

  /**
  * Creates a url of a user to log in.
  *  @param destinationUrl - url to return when after visiting login portal.
  * @return String - url which redirects to login portal.
  */
  public String createLoginUrl(String destinationUrl) {
    return userService.createLoginURL(destinationUrl);
  }

  /**
   * Stores the userInfo entity in the datastore.
   * @param request - request from the UserInfoModalBox component.
   */
  public void storeUserInfoEntityInDatastore(HttpServletRequest request) {
    String firstName = getParameter(request, "first-name").orElse("");
    String lastName = getParameter(request, "last-name").orElse("");
    String userName = getParameter(request, "username").orElse("");
    String favoriteStore = getParameter(request, "favorite-store").orElse("");

    String id = userService.getCurrentUser().getUserId();

    // Do not create another entity to set nickname if it already exists.
    Entity userInfoEntity = getUserInfoEntity(id).orElseGet(() -> {
      Entity info = new Entity("UserInfo");
      info.setProperty("id", id);
      return info;
    });

    userInfoEntity.setProperty("firstName", firstName);
    userInfoEntity.setProperty("lastName", lastName);
    userInfoEntity.setProperty("userName", userName);
    userInfoEntity.setProperty("favoriteStore", favoriteStore);

    datastore.put(userInfoEntity);
  }

  private Optional<String> getParameter(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getParameter(name));
  }

  /**
   * Returns the UserInfo entity with user id.
   * Given id is not of UserInfo kind but a field of that kind.
   * @param id - id of the user who is logged in.
   */
  private Optional<Entity> getUserInfoEntity(String id) {
    
    Query query =
        new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    return Optional.ofNullable(results.asSingleEntity());
  }
  
  /**
   * Creates UserInfo object encapsulating user data.
   * @return UserInfo - wrapper object for user information and logout url.
   */
  private UserInfo createUserInfo() {
    User user = userService.getCurrentUser();
    String logoutUrl = userService.createLogoutURL("/");
    String firstName = "";
    String lastName = "";
    String userName = "";
    String favoriteStore = "";
    
    Optional<Entity> optEntity = getUserInfoEntity(user.getUserId());
    
    // If the entity is present then update the fields.
    if (optEntity.isPresent()) {
      Entity userInfoEntity = optEntity.get();
      firstName = (String) userInfoEntity.getProperty("firstName");
      lastName = (String) userInfoEntity.getProperty("lastName");
      userName = (String) userInfoEntity.getProperty("userName");
      favoriteStore = (String) userInfoEntity.getProperty("favoriteStore");
    }

    UserInfo userInfo = UserInfo.builder()
        .setFirstName(firstName)
        .setLastName(lastName)
        .setUserName(userName)
        .setFavoriteStore(favoriteStore)
        .setEmail(user.getEmail())
        .setUserId(user.getUserId())
        .setLogOutUrl(logoutUrl)
        .build();

    return userInfo;
  }
}

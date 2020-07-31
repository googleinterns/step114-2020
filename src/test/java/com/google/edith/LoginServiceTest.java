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

package com.google.edith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.common.collect.ImmutableMap;
import com.google.edith.services.LoginService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class LoginServiceTest {

  private Map<String, Object> map =
      ImmutableMap.of("com.google.appengine.api.users.UserService.user_id_key", "12345");

  private LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(
              new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig())
          .setEnvAttributes(map)
          .setEnvIsLoggedIn(true)
          .setEnvAuthDomain("gmail")
          .setEnvIsAdmin(true)
          .setEnvEmail("user@gmail.com");

  private LoginService loginService;
  private final UserService userService = UserServiceFactory.getUserService();
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    loginService = new LoginService(userService, datastore);
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Mock HttpServletRequest request;

  // Checks the login status of the user.
  @Test
  public void checkUserLoggedIn_returnsUserLoggedInStatus() {
    assertTrue(loginService.checkUserLoggedIn());
    assertTrue(userService.isUserLoggedIn());
  }

  // Checks if the entity is stored correctly in Datatstore.
  @Test
  public void storeUserInfoEntityInDatastore_addsOneUserInfoEntity() {
    retrieveStubUserInfo(request);
    loginService.storeUserInfoEntityInDatastore(request);
    assertEquals(
        1,
        datastore.prepare(new Query("UserInfo")).countEntities(FetchOptions.Builder.withLimit(10)));
  }

  // Checks that only one entity is created for a user.
  @Test
  public void storeUserInfoEntityInDatastore_ifSameEntityExists_doNotStore() {
    retrieveStubUserInfo(request);
    // Call storeUserInfoEntityInDatastore() twice to
    // mimic storing UserInfo entity twice for same user.
    loginService.storeUserInfoEntityInDatastore(request);
    loginService.storeUserInfoEntityInDatastore(request);
    assertEquals(
        1,
        datastore.prepare(new Query("UserInfo")).countEntities(FetchOptions.Builder.withLimit(10)));
  }

  // Checks if the JSON created has all the user info fields.
  @Test
  public void createJsonOfUserInfo_containsAllFieldsOfUserInfo() {
    retrieveStubUserInfo(request);

    String userInfo = loginService.createJsonOfUserInfo();

    assertTrue(userInfo.contains("firstName"));
    assertTrue(userInfo.contains("lastName"));
    assertTrue(userInfo.contains("favoriteStore"));
    assertTrue(userInfo.contains("email"));
    assertTrue(userInfo.contains("logOutUrl"));
  }

  // Creates a stub user info for testing.
  private void retrieveStubUserInfo(HttpServletRequest request) {
    when(request.getParameter("first-name")).thenReturn("testfirst");
    when(request.getParameter("last-name")).thenReturn("testlast");
    when(request.getParameter("username")).thenReturn("testuser");
    when(request.getParameter("favorite-store")).thenReturn("teststore");
  }
}

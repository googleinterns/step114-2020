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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.services.LoginService;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

  private Map<String, Object> myMap = new HashMap<String, Object>() {{
        put("com.google.appengine.api.users.UserService.user_id_key", "12345");
    }};
  
  private LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),new LocalUserServiceTestConfig())
      .setEnvAttributes(myMap)
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
  
  @Mock
  HttpServletRequest request;

  @Test
  public void checkIfUserIsLoggedIn() {
    assertTrue(loginService.checkUserLoggedIn());
  }
  
  @Test
  public void storeUserInfoIfUnique() {
    retrieveStubUserInfo(request);
    loginService.storeUserInfoEntityInDatastore(request);
    assertEquals(1, datastore.prepare(new Query("UserInfo")).countEntities());
  }

  @Test
  public void doNotStoreMakeUserInfoForSameUser() {
    retrieveStubUserInfo(request);
    // Call storeUserInfoEntityInDatastore() twice to
    // mimick storing UserInfo entity twice for same user.
    loginService.storeUserInfoEntityInDatastore(request);
    loginService.storeUserInfoEntityInDatastore(request);
    assertEquals(1, datastore.prepare(new Query("UserInfo")).countEntities());
  }

  @Test
  public void storeUserInfoForDifferentUser() {
    retrieveStubUserInfo(request);
    loginService.storeUserInfoEntityInDatastore(request);
    Map<String, Object> map = new HashMap<String, Object>() {{
        put("com.google.appengine.api.users.UserService.user_id_key", "6789");
    }};
    // Set a new testhelper with different userId to mimick a different user.
    testHelper = testHelper.setEnvAttributes(map).setUp();
    loginService.storeUserInfoEntityInDatastore(request);
    System.out.println(userService.getCurrentUser().getUserId());
    assertEquals(2, datastore.prepare(new Query("UserInfo")).countEntities());
  }

  private void retrieveStubUserInfo(HttpServletRequest request) {
    when(request.getParameter("first-name")).thenReturn("testfirst");
    when(request.getParameter("last-name")).thenReturn("testlast");
    when(request.getParameter("username")).thenReturn("testuser");
    when(request.getParameter("favorite-store")).thenReturn("teststore");
  }
}

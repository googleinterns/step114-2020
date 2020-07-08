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

import com.google.edith.servlets.LoginServlet;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class LoginServletTest {
  
  private Map<String, Object> myMap = new HashMap<String, Object>() {{
        put("userId", "12345");
    }};
  private final LocalServiceTestHelper loggedInTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
      .setEnvAttributes(myMap)
      .setEnvIsLoggedIn(true)
      .setEnvAuthDomain("gmail")
      .setEnvIsAdmin(true)
      .setEnvEmail("user@gmail.com");
      
  private final LocalServiceTestHelper loggedOutTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
      .setEnvIsLoggedIn(false);
  
  private final UserService userService = UserServiceFactory.getUserService();
  private LoginServlet loginServlet;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    loginServlet = new LoginServlet();
  }

  @Test
  public void testWhenLoggenIn() throws IOException, ServletException {
    loggedInTestHelper.setUp();
    assertTrue(userService.isUserLoggedIn());
    User currentLoggedInUser = userService.getCurrentUser();

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    loginServlet.doGet(request, response);
    verify(response, atLeast(1)).getWriter();
    loggedInTestHelper.tearDown();
  }

  @Test
  public void testWhenLoggedOut() throws IOException, ServletException {
    loggedOutTestHelper.setUp();
    assertFalse(userService.isUserLoggedIn());
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    loginServlet.doGet(request, response);
    verify(response).sendRedirect(captor.capture());
    assertEquals(userService.createLoginURL("/"), captor.getValue());
    loggedOutTestHelper.tearDown();
  }
}
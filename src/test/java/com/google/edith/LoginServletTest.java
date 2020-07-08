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
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

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
  private final LocalServiceTestHelper loggedInTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
      .setEnvIsLoggedIn(true)
      .setEnvAuthDomain("gmail")
      .setEnvIsAdmin(true)
      .setEnvEmail("user@gmail.com");
      
  private final LocalServiceTestHelper loggedOutTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
      .setEnvIsLoggedIn(false);
  
  private final UserService userService = UserServiceFactory.getUserService();

  private final StringWriter stringWriter = new StringWriter();
  private final PrintWriter writer = new PrintWriter(stringWriter);

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testWhenLoggenIn() throws IOException, ServletException {
    loggedInTestHelper.setUp();
    assertTrue(userService.isUserLoggedIn());
    User currentLoggedInUser = userService.getCurrentUser();
    //new LoginServlet().doGet(request, response);
    loggedInTestHelper.tearDown();
  }

  @Test
  public void testWhenLoggedOut() throws Exception {
    loggedOutTestHelper.setUp();
    assertFalse(userService.isUserLoggedIn());
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    new LoginServlet().doGet(request, response);
    verify(response).sendRedirect(captor.capture());
    assertEquals(userService.createLoginURL("/"), captor.getValue());
    loggedOutTestHelper.tearDown();
  }
}
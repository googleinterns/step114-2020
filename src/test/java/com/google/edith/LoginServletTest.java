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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.common.collect.ImmutableMap;
import com.google.edith.servlets.LoginServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class LoginServletTest {
  private Map<String, Object> map =
      ImmutableMap.of("com.google.appengine.api.users.UserService.user_id_key", "12345");

  private final LocalServiceTestHelper loggedInTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
          .setEnvAttributes(map)
          .setEnvIsLoggedIn(true)
          .setEnvAuthDomain("gmail")
          .setEnvIsAdmin(true)
          .setEnvEmail("user@gmail.com");

  private final LocalServiceTestHelper loggedOutTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig()).setEnvIsLoggedIn(false);

  private final UserService userService = UserServiceFactory.getUserService();
  private LoginServlet loginServlet;

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    loginServlet = new LoginServlet();
  }

  @Test
  // Check if the servlet calls getWriter() method.
  public void checks_ifUserLoggedIn_returnsUserInfo() throws IOException, ServletException {
    loggedInTestHelper.setUp();
    assertTrue(userService.isUserLoggedIn());
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    loginServlet.doGet(request, response);
    verify(response, atLeast(1)).getWriter();
    loggedInTestHelper.tearDown();
  }

  @Test
  // Check if the servlet returns with user information if logged-in.
  public void returns_jsonWithCorrect_userInfo() throws IOException, ServletException {
    loggedInTestHelper.setUp();
    assertTrue(userService.isUserLoggedIn());

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    loginServlet.doGet(request, response);
    writer.flush();
    String returnedJson = stringWriter.toString();
    // JSON must contains all fields of UserInfo.
    assertTrue(returnedJson.contains("email"));
    assertTrue(returnedJson.contains("userId"));
    assertTrue(returnedJson.contains("logOutUrl"));
    loggedInTestHelper.tearDown();
  }

  @Test
  // Check if the servlet returns log-in information if logged-out.
  public void checks_ifUserLoggedIn_createsLogInUrl() throws IOException, ServletException {
    loggedOutTestHelper.setUp();
    assertFalse(userService.isUserLoggedIn());
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    loginServlet.doGet(request, response);
    verify(response).sendRedirect(captor.capture());
    assertEquals(userService.createLoginURL("/"), captor.getValue());
    loggedOutTestHelper.tearDown();
  }
}

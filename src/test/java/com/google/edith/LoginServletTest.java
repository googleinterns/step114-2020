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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.edith.interfaces.LoginInterface;
import com.google.edith.servlets.LoginServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class LoginServletTest {

  private LoginServlet loginServlet;

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  @Mock LoginInterface loginImplementation;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    loginServlet = new LoginServlet(loginImplementation);
  }

  @Test
  // Check if the servlet calls getWriter() method.
  public void doGet_whenUserLoggedIn_callsGetWriterMethod() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(loginImplementation.checkUserLoggedIn()).thenReturn(true);
    when(response.getWriter()).thenReturn(writer);

    loginServlet.doGet(request, response);

    verify(response, times(1)).getWriter();
  }

  @Test
  // Check if the servlet calls checkUserLoggedIn() and createJsonFromUserInfo method of LoginInterface.
  public void doGet_whenUserLoggedIn_callsRequiredServiceMethods() throws IOException {
    when(loginImplementation.checkUserLoggedIn()).thenReturn(true);
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    loginServlet.doGet(request, response);

    verify(loginImplementation, times(1)).checkUserLoggedIn();
    verify(loginImplementation, times(1)).createJsonFromUserInfo();
  }

  @Test
  // Check if the servlet calls createLogin() method of LoginInterface.
  public void doGet_whenUserLoggedOut_callsCreateLoginMethod() throws IOException {
    when(loginImplementation.checkUserLoggedIn()).thenReturn(false);
    when(loginImplementation.createLoginUrl("/")).thenReturn("/logIn");

    loginServlet.doGet(request, response);

    verify(loginImplementation, times(1)).createLoginUrl("/");
    verify(response, times(1)).sendRedirect("/logIn");
  }

  @Test
  // Check if storeUserInfoEntityInDatastore method is called when the user is logged in.
  public void doPost_whenUserLoggedIn_callStoreUserInfoEntityInDatastore() throws IOException {
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    when(loginImplementation.checkUserLoggedIn()).thenReturn(true);

    loginServlet.doPost(request, response);

    verify(loginImplementation, times(1)).storeUserInfoEntityInDatastore(request);
    verify(response).sendRedirect(captor.capture());
    assertEquals("/index.html", captor.getValue());
  }

  @Test
  // Check if storeUserInfoEntityInDatastore method is not called when the user is logged out.
  public void doPost_whenUserLoggedOut_doesNotCallStoreUserInfoEntityInDatastore()
      throws IOException {
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    when(loginImplementation.checkUserLoggedIn()).thenReturn(false);

    loginServlet.doPost(request, response);

    verify(loginImplementation, times(0)).storeUserInfoEntityInDatastore(request);
    verify(response).sendRedirect(captor.capture());
    assertEquals("/index.html", captor.getValue());
  }
}

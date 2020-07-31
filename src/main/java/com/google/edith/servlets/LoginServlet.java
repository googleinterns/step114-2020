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

package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.edith.interfaces.LoginInterface;
import com.google.edith.services.LoginService;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that checks if user is logged in. If user is logged in then provide user related info,
 * otherwise redirects to login url.
 */
@WebServlet("/login")
public final class LoginServlet extends HttpServlet {

  private LoginInterface loginImpl;

  public LoginServlet() {
    this.loginImpl =
        new LoginService(
            UserServiceFactory.getUserService(), DatastoreServiceFactory.getDatastoreService());
  }

  public LoginServlet(LoginInterface loginImpl) {
    this.loginImpl = loginImpl;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    if (loginImpl.checkUserLoggedIn()) {
      String json = loginImpl.createJsonOfUserInfo();
      response.setContentType("application/json");
      response.getWriter().println(json);
    } else {
      String loginUrl = loginImpl.createLoginUrl("/");
      response.sendRedirect(loginUrl);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // User who are not logged in can not upload their info. This is handled in FE too.
    if (loginImpl.checkUserLoggedIn()) {
      loginImpl.storeUserInfoEntityInDatastore(request);
    }
    response.sendRedirect("/index.html");
  }
}

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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that checks if user is logged in.
 * If user is logged in then provide user related info,
 * otherwise redirects to login url..
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      Gson gson = new Gson();
      User loggedInUser = userService.getCurrentUser();
      String logoutUrl = userService.createLogoutURL("/");

      String json = gson.toJson(createUserInfo(loggedInUser, logoutUrl));
      response.setContentType("application/json");
      response.getWriter().println(json);
    } else {
      String loginUrl = userService.createLoginURL("/");
      response.sendRedirect(loginUrl);
    }
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendRedirect("/index.html");
  }
  
  /**
   * Creates UserInfo object encapsulating user data.
   * @param user - User object which represents currently logged in uyser.
   * @param logoutUrl - url to logout from the app.
   * @return UserInfo - wrapper object for user information and logout url.
   */
  private UserInfo createUserInfo(User user, String logoutUrl) {
    UserInfo userInfo = UserInfo.builder()
        .setEmail(user.getEmail())
        .setUserId(user.getUserId())
        .setLogOutUrl(logoutUrl)
        .build();
        
    return userInfo;
  }
}
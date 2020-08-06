package com.google.edith;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/grocery-list-query")
public class GroceryListServlet extends HttpServlet {

  private final UserService userService;
  private final DatastoreService datastoreService;
  private final QueryItems queryItems;

  public GroceryListServlet(
      UserService userService, DatastoreService datastoreService, QueryItems queryItems) {
    this.userService = userService;
    this.datastoreService = datastoreService;
    this.queryItems = queryItems;
  }

  public GroceryListServlet() {
    this.userService = UserServiceFactory.getUserService();
    this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    this.queryItems = new QueryItems(datastoreService, userService);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String expiredItems = queryItems.findExpiredItems();
    response.setContentType("application/json");
    response.getWriter().println(expiredItems);
  }
}

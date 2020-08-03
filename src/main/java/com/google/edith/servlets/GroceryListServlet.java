package com.google.edith;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/grocery-list-query")
public class GroceryListServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    QueryItems queryItems = new QueryItems(DatastoreServiceFactory.getDatastoreService(),
        UserServiceFactory.getUserService());
    String expiredItems = queryItems.findExpiredItems();
    response.setContentType("application/json");
    response.getWriter().println(expiredItems);
  }
}

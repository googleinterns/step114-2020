package com.google.edith;

import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/grocery-list-query")
public class GroceryListServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // call Prashant's file and get a list of receipts

    QueryItems queryItems = new QueryItems();
    String items = queryItems.findExpiredItems(receipt);
    response.setContentType("application/json");
    response.getWriter().println(items);
  }
}
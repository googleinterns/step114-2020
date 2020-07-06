package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet that returns some example content.
*/
@WebServlet("/receipt")
public class ReceiptServlet extends HttpServlet {

  private final ReceiptDataService dataservice = new ReceiptDataService();
  private static List<GroceryItem> groceryList = new ArrayList<GroceryItem>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    dataservice.storeReceipt(groceryList);
    //query the deals servlet
    //groceryList = new ArrayList<GroceryItem>();
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String itemName = request.getParameter("itemName");
    Double itemPrice = Double.parseDouble(request.getParameter("itemPrice"));
    Integer itemQuantity = Integer.parseInt(request.getParameter("itemQuantity"));
    GroceryItem item = new GroceryItem(itemName, itemPrice, itemQuantity);
    groceryList.add(item);
  }
}

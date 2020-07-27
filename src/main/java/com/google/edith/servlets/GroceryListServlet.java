package com.google.edith;

import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import java.io.IOException;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/grocery-list-query")
public class GroceryListServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // TODO: replace this with call to prashant's servlet
    Item receiptItem =
      new Item("185804764220139124118",
        "Apple Juice",
        (float) 5.99,
        1,
        "unknown category",
        "6.0 Days");
        /**
    Item receiptItem =
      Item.builder()
            .setUserId("185804764220139124118")
            .setName("Apple Juice")
            .setPrice((float) 5.99)
            .setQuantity(1)
            .setCategory("unknown category")
            .setExpireDate("2020-07-16")
            .build();*/
    Item[] items = new Item[1];
    items[0] = receiptItem;
    Receipt receipt =
      new Receipt(
        "185804764220139124118",
        "whole Foods",
        "2020-07-16",
        "Receipt",
        "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c",
        1,
        items);


    QueryItems queryItems = new QueryItems();
    String expiredItems = queryItems.findExpiredItems(receipt);
    response.setContentType("application/json");
    response.getWriter().println(expiredItems);
  }
}

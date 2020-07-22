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

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    String datePhrase = dateFormat.format(date));

    for (Receipt receipt: receipts) {
      Items[] items = receipt.getItems();
      for (Item item: items) {
        String expiration = item.getExpiration();
      }
    }
  }

}
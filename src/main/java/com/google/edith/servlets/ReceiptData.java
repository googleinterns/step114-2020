package com.google.edith.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ReceiptData {

  public Receipt extractReceiptData() throws IOException {
    
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    ExtractReceipt extractReceipt = new ExtractReceipt();
    List<Map<String, String>> items = extractReceipt.extractReceipt();
    Item[] parsedItems = createReceiptItems(user, items);
    Receipt parsedReceipt = createReceipt(user, parsedItems);
    return parsedReceipt;
  }
  
  private Receipt createReceipt(User user, Item[] items) {
    String expenditureName = ReceiptFileHandlerServlet.getExpenditureName();
    String blobKey = ReceiptFileHandlerServlet.getFileBlobKey();
    float totalPrice = calculateTotalPrice(items);
    Receipt userReceipt = new Receipt(user.getUserId(), "unknown store name", "unknown date", expenditureName, blobKey, totalPrice, items);
    return userReceipt;
  }

  private Item[] createReceiptItems(User user, List<Map<String, String>> extractedData) {
    int index = 0;
    int totalItems = extractedData.size();
    Item[] items = new Item[totalItems];
    while (index < totalItems) {
      Map<String, String> itemFields = extractedData.get(index);
      Item receiptItem = new Item(
            user.getUserId(),
            itemFields.get("itemName"),
            Float.parseFloat(itemFields.get("itemPrice")),
            0,
            "unknown category",
            "unknown date");
      items[index++] = receiptItem;
    }
    return items;
  }

  private float calculateTotalPrice(Item[] items) {
    float totalPrice = 0;
    for (Item item : items) {
      totalPrice += item.getPrice();
    }
    return totalPrice;
  }
}

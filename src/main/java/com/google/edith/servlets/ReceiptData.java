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

  public static Receipt extractReceiptData(String blobKey, String expenditureName) throws IOException {
    
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    List<Map<String, String>> items = ExtractReceipt.extractReceipt(blobKey);
    Item[] parsedItems = createReceiptItems(user, items);
    Receipt parsedReceipt = createReceipt(blobKey, user, parsedItems, expenditureName);
    return parsedReceipt;
  }
  
  private static Receipt createReceipt(String blobKey, User user, Item[] items, String expenditureName) {
    Receipt userReceipt = new Receipt(user.getUserId(), "unknown store name", "unknown date", expenditureName, blobKey, 0.0f, items);
    return userReceipt;
  }

  private static Item[] createReceiptItems(User user, List<Map<String, String>> extractedData) {
    int index = 0;
    int totalItems = extractedData.size();
    Item[] items = new Item[totalItems];
    while (index < totalItems) {
      Map<String, String> itemFields = extractedData.get(index);
      Item receiptItem = Item.builder()
            .setUserId(user.getUserId())
            .setName(itemFields.get("itemName"))
            .setPrice(Float.parseFloat(itemFields.get("itemPrice")))
            .setQuantity(0)
            .setCategory("unknown category")
            .setExpiration("unknown date")
            .setDate("unknown")
            .build();

      items[index++] = receiptItem;
    }
    return items;
  }
}

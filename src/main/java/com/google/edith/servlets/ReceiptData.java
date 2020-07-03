package com.google.edith.servlets;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ReceiptData {

  public void extractReceiptData() throws FileNotFoundException {
    BufferedReader br = new BufferedReader(new FileReader("/home/prashantneu/step114-2020/src/main/java/com/google/edith/servlets/response.json"));
    JsonElement jelement = new JsonParser().parse(br);
    JsonObject  jobject = jelement.getAsJsonObject();
    JsonArray jarray = jobject.getAsJsonArray("entities");
    HashMap<String, ArrayList<String>> extractedData= getImportantData(jarray);

    Item[] items = createReceiptItems(extractedData);
    Receipt myReceipt = createReceipt(extractedData, items);

    System.out.println(myReceipt);
    for (Item myItem : items) {
      System.out.println(myItem.toString());
    }
  }

  private HashMap<String, ArrayList<String>> getImportantData(JsonArray jarray) {
    HashMap<String, ArrayList<String>> extractedData= new HashMap<>();
    List<String> neededFields = Arrays.asList("supplier_name", "invoice_date", "total_tax_amount",
        "total_amount", "line_item/description","line_item/amount", "line_item/quantity");

    JsonObject  jobject;
    String elementType;
    for (int i = 0; i < jarray.size(); i++) {
      jobject = jarray.get(i).getAsJsonObject();
      elementType = jobject.get("type").getAsString();
      if (neededFields.contains(elementType)) {
        if (extractedData.containsKey(elementType)) {
          extractedData.get(elementType).add(jobject.get("mentionText").getAsString());
        } else {
          ArrayList<String> items = new ArrayList<String>();
          items.add(jobject.get("mentionText").getAsString());
          extractedData.put(elementType, items);
        }
      }
    }
    return extractedData;
  }

  private Receipt createReceipt(HashMap<String, ArrayList<String>> extractedData, Item[] items) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    
    String expenditureName = ReceiptFileHandlerServlet.getExpenditureName();
    String blobKey = ReceiptFileHandlerServlet.getFileBlobKey();
    
    Receipt userReceipt = new Receipt(user.getUserId(), extractedData.get("supplier_name").get(0), extractedData.get("invoice_date").get(0), expenditureName, blobKey, extractedData.get("total_tax_amount").get(0), items);
    return userReceipt;
  }

  private Item[] createReceiptItems(HashMap<String, ArrayList<String>> extractedData) {
      UserService userService = UserServiceFactory.getUserService();
      User user = userService.getCurrentUser();
      int index = 0;
      int totalItems = extractedData.get("line_item/description").size();
      Item[] items = new Item[totalItems];
      while (index < totalItems) {
        Item receiptItem = new Item(user.getUserId(), extractedData.get("line_item/description").get(index), extractedData.get("line_item/amount").get(index), Integer.parseInt(extractedData.get("line_item/quantity").get(index)));
        items[index] = receiptItem;
        index++;
      }
      return items;
  }
}

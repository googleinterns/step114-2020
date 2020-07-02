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

public class ReceiptData {

  public void printData() throws FileNotFoundException {
    BufferedReader br = new BufferedReader(new FileReader("/home/prashantneu/step114-2020/src/main/java/com/google/edith/servlets/response.json"));
    JsonElement jelement = new JsonParser().parse(br);
    JsonObject  jobject = jelement.getAsJsonObject();
    JsonArray jarray = jobject.getAsJsonArray("entities");
    HashMap<String, ArrayList<String>> extractedData= getImportantData(jarray);
    for (Map.Entry<String, ArrayList<String>> entry : extractedData.entrySet()) {
      String key = entry.getKey();
      ArrayList<String> value = entry.getValue();
      System.out.println("===============================");
      System.out.println(key);
      value.forEach((arrayValue) -> System.out.println(arrayValue));
      System.out.println("===============================");
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
        if (!extractedData.containsKey(elementType)) {
          ArrayList<String> items = new ArrayList<String>();
          items.add(jobject.get("mentionText").getAsString());
          extractedData.put(elementType, items);
        } else {
          extractedData.get(elementType).add(jobject.get("mentionText").getAsString());
        }
      }
    }
    return extractedData;
  }
}

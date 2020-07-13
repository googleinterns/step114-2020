package com.google.edith;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import java.net.URL;

/** Processes data file of product expiration information
  * to populate future user grocery lists. */
public class ShelfDataReader {

  /** Finds the specified product in the file . */
  public String readFile(String itemName) {
    URL jsonresource = getClass().getClassLoader().getResource("foodkeeper.json");
    File shelfLifeData = new File(jsonresource.getFile());

    JsonParser jsonParser = new JsonParser();
    try (FileReader reader = new FileReader(shelfLifeData)) {
      Gson gson = new Gson();

      JsonObject data = (JsonObject) jsonParser.parse(reader).getAsJsonObject();
      JsonArray sheets = data.getAsJsonArray("sheets");
      JsonObject productList = sheets.get(2).getAsJsonObject();
      JsonArray productListData = productList.getAsJsonArray("data");

      for (int i = 0; i < productListData.size(); i++) {
        JsonArray product = productListData.get(i).getAsJsonArray();
        JsonObject nameObject = product.get(2).getAsJsonObject();
        String productName = nameObject.get("Name").getAsString();
        if (productName.equals(itemName)) {
          findTime(product);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    } catch (IOException e) {
      System.out.println("IOException");
    }

    return "";
  }

  private int findTime(JsonArray product) {
    Gson gson = new Gson();

    for (int i = 5; i < product.size(); i++){
      JsonObject productTimeElement = product.get(i).getAsJsonObject();
      String json = gson.toJson(productTimeElement);
      String[] keyValuePair = json.split(":");

      if (keyValuePair.length == 2) {
        String value = keyval[1];
        value = value.substring(0, value.length()-1);
        System.out.println(value);
      }
    }
    return 0;
  }
}

package com.google.edith;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/** Processes data file of product expiration information to populate future user grocery lists. */
public class ShelfDataReader {

  /** Finds the specified product in the file. */
  public String readFile(String itemName) {
    URL jsonresource = getClass().getClassLoader().getResource("foodkeeper.json");
    File shelfLifeData = new File(jsonresource.getFile());

    JsonParser jsonParser = new JsonParser();
    List<JsonArray> potentialMatches = new ArrayList<JsonArray>();
    try (FileReader reader = new FileReader(shelfLifeData)) {
      Gson gson = new Gson();

      JsonObject data = (JsonObject) jsonParser.parse(reader).getAsJsonObject();
      JsonArray sheets = data.getAsJsonArray("sheets");
      JsonObject productList = sheets.get(2).getAsJsonObject();
      JsonArray productListData = productList.getAsJsonArray("data");

      for (int i = 0; i < productListData.size(); i++) {
        JsonArray product = productListData.get(i).getAsJsonArray();
        JsonObject nameObject = product.get(2).getAsJsonObject();
        String productName = nameObject.get("Name").getAsString().toLowerCase();

        if (productName.equals(itemName.toLowerCase())) {
          potentialMatches.add(product);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    } catch (IOException e) {
      System.out.println("IOException");
    }

    if (potentialMatches.size() == 0) {
      return "no shelf life data found";
    } else {
      return findTime(potentialMatches.get(0));
    }
  }
  
  /**
    * Retrieves the shelf life data of the specified product. Only looks through pantry and
    * refrigeration data, as freezing tends to be longer term. Items also tend to have freezing and
    * fridge data or freezing and pantry data, so removing freezing makes it so that items have only
    * one set of expiration data.
    */
  private String findTime(JsonArray product) {
    Gson gson = new Gson();
    String result = "";

    for (int i = 5; i < 27; i++) {
      JsonObject productTimeElement;
      try {
        productTimeElement = product.get(i).getAsJsonObject();
      } catch (IndexOutOfBoundsException e) {
        break;
      }
      String json = gson.toJson(productTimeElement);
      String[] jsonKeyValuePair = json.split(":");

      if (jsonKeyValuePair.length == 2) {
        String expireData = jsonKeyValuePair[1];
        expireData = expireData.substring(0, expireData.length() - 1);
        try {
          Double.parseDouble(expireData);
          result += expireData + " ";
        } catch (NumberFormatException e) {
          String timeUnit = expireData.substring(1, expireData.length() - 1);
          if (timeUnit.equals("Days") || timeUnit.equals("Weeks") || timeUnit.equals("Months")) {
            result += timeUnit + " ";
          }
        }
      }
    }

    result = result.substring(0, result.length() - 1);
    return result;
  }
}

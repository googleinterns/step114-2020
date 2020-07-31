package com.google.edith;

import com.google.common.base.Splitter;
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
public final class ShelfDataReader {
  private static final String NO_EXPIRATION = "NO_EXPIRATION";

  /** Finds the specified product in the file. */
  public String readFile(String itemName) {
    URL jsonResource = getClass().getClassLoader().getResource("foodkeeper.json");
    File shelfLifeData = new File(jsonResource.getFile());

    JsonParser jsonParser = new JsonParser();
    List<JsonArray> potentialMatches = new ArrayList<JsonArray>();
    try (FileReader reader = new FileReader(shelfLifeData)) {
      JsonObject data = (JsonObject) jsonParser.parseReader(reader).getAsJsonObject();
      JsonArray sheets = data.getAsJsonArray("sheets");
      /*
       * Get(2) is used because the product data starts at that index, previous data is all
       * header data on the file.
       */
      JsonObject productList = sheets.get(2).getAsJsonObject();
      JsonArray productListData = productList.getAsJsonArray("data");

      for (int i = 0; i < productListData.size(); i++) {
        JsonArray product = productListData.get(i).getAsJsonArray();
        JsonObject nameObject = product.get(2).getAsJsonObject();
        String productName = nameObject.get("Name").getAsString();
        if (productName.equals(itemName)) {
          potentialMatches.add(product);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    if (potentialMatches.isEmpty()) {
      return NO_EXPIRATION;
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

    /**
     * The bounds 6 and 27 correspond to the array indices in the JsonArray that contain shelf life
     * data. More specifically, the lower bounds marks where data about the type of product stops
     * and the data about expiration date begins, and the higher bound is where freezing data begins
     * which, as previously mentioned, is being excluded to minimize duplicate data and very long
     * term expiration dates.
     */
    for (int i = 6; i < 27; i++) {
      JsonObject productTimeElement;
      try {
        productTimeElement = product.get(i).getAsJsonObject();
      } catch (IndexOutOfBoundsException e) {
        break;
      }
      String json = gson.toJson(productTimeElement);
      List<String> jsonKeyValuePair = Splitter.on(":").splitToList(gson.toJson(productTimeElement));

      if (jsonKeyValuePair.size() == 2) {
        String expireData = jsonKeyValuePair.get(1);
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

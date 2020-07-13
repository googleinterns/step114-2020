package com.google.edith;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ShelfDataReader {

  public void readFile(String itemName) {
    URL jsonresource = getClass().getClassLoader().getResource("foodkeeper.json");
    File shelfLifeData = new File(jsonresource.getFile());

    JsonParser jsonParser = new JsonParser();
    try (FileReader reader = new FileReader(shelfLifeData)) {
      Object obj = jsonParser.parse(reader);
      obj = jsonParser.parse(reader);
      obj = jsonParser.parse(reader);
      System.out.println(obj);
      
    } catch (FileNotFoundException e) {
      System.out.println("file not found");
    } catch (IOException e) {
      System.out.println("IOException");
    }
  }
}
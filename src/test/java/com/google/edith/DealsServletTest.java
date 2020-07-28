package com.google.edith.servlets;

import com.google.edith.DealsServlet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class DealsServletTest {
  @Test
  public void doPost_itemNameInCsv_respondsWithCheapestStore() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    Item receiptItem =
        new Item("185804764220139124118",
            "Apple Juice",
            (float) 5.99,
            1,
            "unknown category",
            "unknown date");
    Item[] items = new Item[1];
    items[0] = receiptItem;
    Receipt receipt =
        new Receipt(
            "185804764220139124118",
            "whole Foods",
            "unknown date",
            "Receipt",
            "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c",
            0,
            items);

    Gson gson = new Gson();
    String json = gson.toJson(receipt);
    JsonParser parser = new JsonParser();
    JsonObject inputJson = parser.parse(json).getAsJsonObject();

    Mockito.when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(inputJson.toString())));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    Mockito.verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Kroger"));
  }

  @Test
  public void doPost_randomStringInput_respondsWithNoDealFound() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    Item receiptItem =
        new Item("185804764220139124118",
            "no deal",
            (float) .99,
            0,
            "unknown category",
            "unknown date");
    Item[] items = new Item[1];
    items[0] = receiptItem;
    Receipt receipt =
        new Receipt(
            "185804764220139124118",
            "whole Foods",
            "unknown date",
            "Receipt",
            "L2dzL2VkaXRoLXJlY2VpcHRzL1NMY1gwX1VZczduVlBJaFBPV3dkY2c",
            0,
            items);

    Gson gson = new Gson();
    String json = gson.toJson(receipt);
    JsonParser parser = new JsonParser();
    JsonObject inputJson = parser.parse(json).getAsJsonObject();

    Mockito.when(request.getReader())
        .thenReturn(new BufferedReader(new StringReader(inputJson.toString())));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    Mockito.verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("no deal found"));
  }
}

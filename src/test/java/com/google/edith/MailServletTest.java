package com.google.edith;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.edith.servlets.Item;
import com.google.edith.servlets.MailServlet;
import com.google.edith.servlets.Receipt;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MailServletTest {
  @Test
  public void doPost_itemExpired_respondsWithEmail() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    Item receiptItem =
            Item.builder()
            .setUserId("185804764220139124118")
            .setName("Apple Juice")
            .setPrice((float) 5.99)
            .setQuantity(1)
            .setDate("date")
            .setCategory("unknown category")
            .setExpiration("unknown date")
            .build();
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
    String json = gson.toJson(Arrays.asList(receipt));

    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json.toString())));

    StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new MailServlet(true).doPost(request, response);

    verify(request, atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(
        stringWriter.toString().contains("Apple Juice")
            && stringWriter.toString().contains("Hello!"));
  }
}

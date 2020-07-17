package com.google.edith;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

public class DealsServletTest  {
/**
  @Test
  public void doPost_itemNameInCsv_respondsWithCheapestStore() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);       
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    String data = "{data: {userId: 185804764220139124118, storeName: G, date: unknown date, name: Receipt, fileUrl: encoded_gs_key:L2dzL2VkaXRoLXJlY2VpcHRzLzJJU2dmeWJTaUpkaEg1azVRTHViVFE, totalPrice: 0, items:[{userId: 185804764220139124118, name: apple juice, price: 0.99, quantity: 1, category: unknown category, expireDate: unknown date}]}";
    Gson gson = new Gson();
    String json = gson.toJson(data);
    Mockito.when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(json)));
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    Mockito.verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Kroger"));
  }

  @Test
  public void doPost_emptyStringInput_respondsWithNoDealFound() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);       
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    String data = "{data: {userId: 185804764220139124118, storeName: G, date: unknown date, name: Receipt, fileUrl: encoded_gs_key:L2dzL2VkaXRoLXJlY2VpcHRzLzJJU2dmeWJTaUpkaEg1azVRTHViVFE, totalPrice: 0, items:[{userId: 185804764220139124118, name: no deal, price: 0.99, quantity: 1, category: unknown category, expireDate: unknown date}}";
    Gson gson = new Gson();
    String json = gson.toJson(data);
    Mockito.when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(json)));
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    Mockito.verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().isEmpty());
  }*/
}

package com.google.edith;

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

  @Test
  public void doPost_itemNameInCsv_respondsWithCheapestStore() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);       
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    String json = "{\"itemName\": \"Apple Juice\"}";
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

    String json = "{\"itemName\": \"\"}";
    Mockito.when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(json)));
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    Mockito.verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("no deal found"));
  }
}

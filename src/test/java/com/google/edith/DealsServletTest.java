package com.google.edith;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class DealsServletTest {

  @Test
  public void doPost_itemNameInCsv_respondsWithCheapestStore() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    String json = "{\"itemName\": \"Apple Juice\"}";
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Kroger"));
  }

  @Test
  public void doPost_emptyStringInput_respondsWithNoDealFound() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    String json = "{\"itemName\": \"\"}";
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new DealsServlet().doPost(request, response);

    verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("NO_STORE"));
  }
}

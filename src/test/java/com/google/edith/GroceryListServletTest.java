package com.google.edith.servlets;

import com.google.edith.GroceryListServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class GroceryListServletTest {

  @Test
  public void doGet_mockDataInServlet_returnsExpectedResult() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new GroceryListServlet().doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Apple Juice"));
  }
}

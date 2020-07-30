package com.google.edith;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.edith.servlets.UserInsightsInterface;
import com.google.edith.servlets.UserStatsServlet;
import com.google.edith.servlets.Item;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;



public final class UserStatsServletTest {
  
  private DatastoreService datastore;
  private final LocalServiceTestHelper testHelper = 
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final Gson gson = new Gson();

  @Before
  public void setUp() {
    testHelper.setUp();
    datastore = DatastoreServiceFactory.getDatastoreService();
  }  

  @Test
  public void testServletGoodInput() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);       
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    JsonObject testJson = new JsonObject();
    testJson.addProperty("itemName", "Corn");
    testJson.addProperty("itemPrice", "5.00");
    testJson.addProperty("itemQuantity", "4");
    testJson.addProperty("itemDate", "2020-07-14");

    String json = gson.toJson(testJson);
    Mockito.when(request.getReader()).thenReturn(
        new BufferedReader(new StringReader(json)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);

    new UserStatsServlet().doPost(request, response);

    Mockito.verify(request, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Item posted"));
  }

  @Test
  public void testServlet() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);       
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    UserInsightsInterface userInsights = Mockito.mock(UserInsightsInterface.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(writer);
    Mockito.when(userInsights.createJson()).thenReturn("");

    new UserStatsServlet(datastore, userInsights).doGet(request, response);
    Mockito.verify(userInsights, Mockito.atLeast(1)).createJson();
  }

}

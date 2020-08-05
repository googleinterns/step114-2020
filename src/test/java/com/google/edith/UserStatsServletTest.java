package com.google.edith;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.servlets.UserInsightsInterface;
import com.google.edith.servlets.UserStatsServlet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public final class UserStatsServletTest {

  private DatastoreService datastore;
  private final LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final Gson gson = new Gson();
  private final String USER_ID = "userId";

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
    testJson.addProperty("itemUserId", "userId");
    testJson.addProperty("itemCategory", "Vegetable");
    testJson.addProperty("itemPrice", "5.00");
    testJson.addProperty("itemQuantity", "4");
    testJson.addProperty("itemDate", "2020-07-14");
    testJson.addProperty("itemReceiptId", "receiptId");

    String json = gson.toJson(testJson);
    when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new UserStatsServlet().doPost(request, response);

    verify(request, Mockito.atLeast(1)).getReader();
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
    when(response.getWriter()).thenReturn(writer);
    when(userInsights.createJson(USER_ID)).thenReturn("");

    new UserStatsServlet(datastore, userInsights).doGet(request, response);
    verify(userInsights, Mockito.atLeast(1)).createJson(USER_ID);
  }
}

package com.google.edith;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
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

  private static DatastoreService DATASTORE;
  private static final HttpServletRequest REQUEST = Mockito.mock(HttpServletRequest.class);
  private static final HttpServletResponse RESPONSE = Mockito.mock(HttpServletResponse.class);
  private static final UserInsightsInterface USER_INSIGHTS =
      Mockito.mock(UserInsightsInterface.class);
  private static final UserService USER_SERVICE = Mockito.mock(UserService.class);
  private static final LocalServiceTestHelper TEST_HELPER =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private static final Gson GSON = new Gson();
  private static final String USER_ID = "userId";

  @Before
  public void setUp() {
    TEST_HELPER.setUp();
    DATASTORE = DatastoreServiceFactory.getDatastoreService();
  }

  @Test
  public void testServlet_doPost_runsCorrectly() throws Exception {
    JsonObject testJson = new JsonObject();
    testJson.addProperty("itemName", "Corn");
    testJson.addProperty("itemUserId", "userId");
    testJson.addProperty("itemCategory", "Vegetable");
    testJson.addProperty("itemPrice", "5.00");
    testJson.addProperty("itemQuantity", "4");
    testJson.addProperty("itemDate", "2020-07-14");

    String json = GSON.toJson(testJson);
    when(REQUEST.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(RESPONSE.getWriter()).thenReturn(writer);

    new UserStatsServlet(DATASTORE, USER_INSIGHTS, USER_SERVICE).doPost(REQUEST, RESPONSE);

    verify(REQUEST, Mockito.atLeast(1)).getReader();
    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Item posted"));
  }

  @Test
  public void testServlet_doGet_runsCorrectly() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(RESPONSE.getWriter()).thenReturn(writer);
    when(USER_INSIGHTS.createJson(USER_ID)).thenReturn("");

    new UserStatsServlet(DATASTORE, USER_INSIGHTS, USER_SERVICE).doGet(REQUEST, RESPONSE);
    verify(USER_INSIGHTS, Mockito.atLeast(1)).createJson(USER_ID);
  }
}

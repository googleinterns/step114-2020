package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.users.UserService;
import com.google.edith.GroceryListServlet;
import com.google.edith.QueryItems;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GroceryListServletTest {
  private GroceryListServlet groceryListServlet;

  @Mock UserService userService;

  @Mock DatastoreService datastoreService;

  @Mock QueryItems queryItems;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    queryItems = Mockito.mock(QueryItems.class);
    groceryListServlet = new GroceryListServlet(userService, datastoreService, queryItems);
  }

  @Test
  public void doGet_mockDataInServlet_returnsExpectedResult() throws Exception {
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    Mockito.when(queryItems.findExpiredItems()).thenReturn("Apple Juice");
    Mockito.when(response.getWriter()).thenReturn(writer);

    groceryListServlet.doGet(request, response);

    writer.flush();
    Assert.assertTrue(stringWriter.toString().contains("Apple Juice"));
  }
}

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
* Servlet that returns some example content.
*/
@WebServlet("/api/v1/data-servlet")
public class DataServlet extends HttpServlet {

  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();  

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println("Hello world from data servlet!");

   
    updateUserStats();
  }


  public void updateUserStats(String userId) {
    
    Query query = new Query("UserStats");
    PreparedQuery results = datastore.prepare(query);
    Entity userStats = results.asList(FetchOptions.Builder.withLimit(10))
                            .stream()
                            .filter(entity -> ((String) entity.getProperty("userId")).equals(userId))
                            .collect(Collectors.toList()).get(0);

    List<String> reciepts = (List<String>) userStats.getProperty("Receipts");
    recipts.add(addRecipt());
    userStats.setProperty("Receipts", receipts);
    datastore.put(userStats);

  }
  
  public void getUserStats() {
    Query query = new Query("UserStats");
    PreparedQuery results = datastore.prepare(query);

  }

  public List<Key> addReceipt() {
    throw new UnsupportedOperationException();
  }

}

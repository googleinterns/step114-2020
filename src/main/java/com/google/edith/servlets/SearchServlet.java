// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.edith.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.edith.services.LoginService;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that searches the datastore for the 
 * entity which has all the properties submitted
 * in the form.
 */
@WebServlet("/search-entity")
public class SearchServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();

    String loggedInUserId = userService.getCurrentUser().getUserId();
    List<Filter> filters = new ArrayList<>();
    filters.add(new FilterPredicate("userId", FilterOperator.EQUAL, loggedInUserId));

    String kind = getParameter(request, "kind").orElse("");

    String name = getParameter(request, "name").orElse("");
    String date = getParameter(request, "date").orElse("");
    String sortOrder = getParameter(request, "sort-order").orElse("");
    String sortOnProperty = getParameter(request, "sort-on").orElse("")
                              .toLowerCase();

    if (!name.isEmpty()) {
      filters.add(new FilterPredicate("name", FilterOperator.EQUAL, name));
    }

    if (!date.isEmpty()) {
      filters.add(new FilterPredicate("date", FilterOperator.EQUAL, date));
    }

    Filter entityFilter = new CompositeFilter(CompositeFilterOperator.AND, filters);

    Query query;
    
    if (kind.equals("Receipt")) {
      query = new Query("Receipt").setFilter(entityFilter);
    } else {
      query = new Query("Item").setFilter(entityFilter);
    }
    
    if (sortOrder.equals("Ascending")) query = query.addSort(sortOnProperty, SortDirection.ASCENDING);
    if (sortOrder.equals("Descending")) query = query.addSort(sortOnProperty, SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);
    List<Entity> entities = results.asList(FetchOptions.Builder.withLimit(10));
    for (Entity entity: entities) {
      System.out.println(entity.toString());
    }
    response.sendRedirect("/");
  }

  private Optional<String> getParameter(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getParameter(name));
  }
}

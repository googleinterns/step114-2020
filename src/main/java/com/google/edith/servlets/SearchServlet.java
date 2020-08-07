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

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.collect.ImmutableList;
import com.google.edith.interfaces.SearchService;
import com.google.edith.services.SearchServiceImpl;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that searches the datastore for the entity which has all the properties submitted in the
 * form.
 */
@WebServlet("/search-entity")
public final class SearchServlet extends HttpServlet {
  private SearchService searchService;
  private ImmutableList<Receipt> receipts;
  private ImmutableList<Item> items;
  private String kind;

  public SearchServlet() {
    this.searchService =
        new SearchServiceImpl(
            DatastoreServiceFactory.getDatastoreService(), UserServiceFactory.getUserService());
  }

  public SearchServlet(SearchService searchService) {
    this.searchService = searchService;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();

    String json = kind.equals("Receipt") ? gson.toJson(receipts) : gson.toJson(items);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    kind = getParameter(request, "kind").orElse("");
    String name = getParameter(request, "name").orElse("");
    String date = getParameter(request, "date").orElse("");
    String sortOrder = getParameter(request, "sort-order").orElse("");
    String sortOnProperty = getParameter(request, "sort-on").orElse("").toLowerCase();

    ImmutableList<Entity> entities =
        searchService.findEntityFromDatastore(name, date, kind, sortOrder, sortOnProperty);

    for (Entity entity : entities) {
      System.out.println(entity);
    }

    if (kind.equals("Receipt")) {
      receipts = searchService.createReceiptObjects(entities);
    } else {
      items = searchService.createItemObjects(entities);
    }
    response.sendRedirect("/#search-results");
  }

  /**
   * Returns Optional of String from form in FE.
   *
   * @param request - id of the user who is logged in.
   * @param name - name of the field.
   * @return Optional<String> -
   */
  private Optional<String> getParameter(HttpServletRequest request, String name) {
    return Optional.ofNullable(request.getParameter(name));
  }
}

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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.gson.Gson;
import com.google.edith.services.ReceiptFileHandlerService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When the Upload Receipt button is clicked then this servlet provides an url for the
 * file to be stored in the Google Cloud Storage Bucket and redirects the request to
 * ReceiptFileHandlerServlet.
 */
@WebServlet("/receipt-file-handler")
public class ReceiptFileHandlerServlet extends HttpServlet {
  
  private Receipt parsedReceipt;
  private ReceiptFileHandlerService receiptFileHandlerService;
  
  private static BlobKey fileBlobKey;
  private static String expenditureName;
  

  public ReceiptFileHandlerServlet() {
    this.receiptFileHandlerService = new ReceiptFileHandlerService(BlobstoreServiceFactory.getBlobstoreService());
  }

  public ReceiptFileHandlerServlet(ReceiptFileHandlerService receiptFileHandlerService) {
    this.receiptFileHandlerService = receiptFileHandlerService;
  }
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    String json = gson.toJson(parsedReceipt);
    System.out.println(json);
    
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    expenditureName = request.getParameter("expense-name") == null ? "unknown" : request.getParameter("expense-name");
    List<FileInfo> fileKeys = receiptFileHandlerService.getUploadedFileUrl(request, "receipt-file").orElse(Collections.emptyList());

    if (fileKeys.isEmpty()) {
      throw new IllegalStateException();
    }

    fileBlobKey = receiptFileHandlerService.getBlobKey(fileKeys);
    parsedReceipt = receiptFileHandlerService.createParsedReceipt();
    
    response.sendRedirect("/");
  }

  public static String getFileBlobKey() {
    return fileBlobKey.getKeyString();
  }

  public static String getExpenditureName() {
    return expenditureName;
  }
}

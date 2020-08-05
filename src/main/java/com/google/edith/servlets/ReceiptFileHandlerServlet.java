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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.gson.Gson;
import com.google.edith.interfaces.ReceiptFileHandlerInterface;
import com.google.edith.services.ReceiptFileHandlerService;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When the Upload Receipt button is clicked then this servlet provides an url for the file to be
 * stored in the Google Cloud Storage Bucket and redirects the request to ReceiptFileHandlerServlet.
 */
@WebServlet("/receipt-file-handler")
public class ReceiptFileHandlerServlet extends HttpServlet {
  
  private static BlobKey fileBlobKey;
  private static String expenditureName;

  private Receipt parsedReceipt;
  private ReceiptFileHandlerInterface receiptFileHandler;

  public ReceiptFileHandlerServlet() {
    this.receiptFileHandler =
        new ReceiptFileHandlerService(BlobstoreServiceFactory.getBlobstoreService());
  }

  public ReceiptFileHandlerServlet(ReceiptFileHandlerInterface receiptFileHandler) {
    this.receiptFileHandler = receiptFileHandler;
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    String json = gson.toJson(parsedReceipt);
    
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    expenditureName = request.getParameter("expense-name") == null ? "unknown" : request.getParameter("expense-name");
    List<FileInfo> fileKeys = receiptFileHandler.getUploadedFileUrl(request, "receipt-file");

    // fileKeys never should be empty as file field in the FE form is required.
    if (fileKeys.isEmpty()) {
      throw new IllegalStateException("file must be uploaded in the form");
    }

    fileBlobKey = receiptFileHandler.getBlobKey(fileKeys);
    parsedReceipt = receiptFileHandler.createParsedReceipt();
    
    response.sendRedirect("/");
  }

  public static String getFileBlobKey() {
    return fileBlobKey.getKeyString();
  }

  public static String getExpenditureName() {
    return expenditureName;
  }
}

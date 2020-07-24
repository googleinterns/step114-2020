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

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * When the Upload Receipt button is clicked then this servlet provides an url for the
 * file to be stored in the Google Cloud Storage Bucket and redirects the request to
 * ReceiptFileHandlerServlet.
 */
@WebServlet("/blobstore-upload-url")
public class BlobstoreUrlServlet extends HttpServlet {
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		UploadOptions uploadOptions = UploadOptions.Builder.withGoogleStorageBucketName("edith-receipts");

		String uploadUrl = blobstoreService.createUploadUrl("/receipt-file-handler", uploadOptions);
   
    response.setContentType("text/html");
    response.getWriter().println(uploadUrl);

  }
}

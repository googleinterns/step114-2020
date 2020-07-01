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
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
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
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("From ReceiptFileHandlerServlet");
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");

        if (blobKeys == null || blobKeys.isEmpty()) {
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
    }
  }
  
  /** Returns a URL that points to the uploaded file, or null if the user didn't upload a file. */
//   private String getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
//     BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//     BlobKey blobKey = blobstoreService.createGsBlobKey(
//         "/gs/" + fileName.getBucketName() + "/" + fileName.getObjectName());
//     blobstoreService.serve(blobKey, resp);
//   }

}
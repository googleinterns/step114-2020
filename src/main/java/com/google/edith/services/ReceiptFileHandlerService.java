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

package com.google.edith.services;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 */
public class ReceiptFileHandlerService {
  private final BlobstoreService blobstoreService;

  public ReceiptFileHandlerService(BlobstoreService blobstoreService) {
    this.blobstoreService = blobstoreService;
  }
  
  /** Redirects the page to a new tab and serves the blob. */
  public void serveBlob(HttpServletResponse response, List<FileInfo> fileKeys) throws IOException {
    BlobKey fileBlobKey = getBlobKey(fileKeys);
    blobstoreService.serve(fileBlobKey, response);
  }

  /**
   * Returns a List of BlobKey that points to the uploaded files
   * in the HTML form or null if the user didn't upload a file.
   */
  public Optional<List<FileInfo>> getUploadedFileUrl(HttpServletRequest request, String formInputElementName) {
    Map<String, List<FileInfo>> fileInfos = blobstoreService.getFileInfos(request);
    return Optional.ofNullable(fileInfos.get(formInputElementName));
  }

  /** Returns a  BlobKey that points to the uploaded file. */
  public BlobKey getBlobKey(List<FileInfo> fileKeys) {
    if (fileKeys.isEmpty()) throw new IllegalStateException();

    FileInfo fileInfo = fileKeys.get(0);
    return blobstoreService.createGsBlobKey(fileInfo.getGsObjectName());
  }
}

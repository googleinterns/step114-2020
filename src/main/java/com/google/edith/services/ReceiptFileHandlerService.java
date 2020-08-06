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
import com.google.appengine.api.blobstore.FileInfo;
import com.google.common.collect.ImmutableList;
import com.google.edith.interfaces.ReceiptFileHandlerInterface;
import com.google.edith.servlets.Receipt;
import com.google.edith.servlets.ReceiptData;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Service that stores file uploaded in Blobstore and serves that blob on succession. */
public final class ReceiptFileHandlerService implements ReceiptFileHandlerInterface {
  private final BlobstoreService blobstoreService;

  public ReceiptFileHandlerService(BlobstoreService blobstoreService) {
    this.blobstoreService = blobstoreService;
  }

  @Override
  public void serveBlob(HttpServletResponse response, List<FileInfo> fileKeys) throws IOException {
    BlobKey fileBlobKey = getBlobKey(fileKeys);
    blobstoreService.serve(fileBlobKey, response);
  }

  @Override
  public ImmutableList<FileInfo> getUploadedFileUrl(
      HttpServletRequest request, String formInputElementName) {

    Map<String, List<FileInfo>> fileInfos = blobstoreService.getFileInfos(request);
    List<FileInfo> uploadedFile = fileInfos.get(formInputElementName);
    return uploadedFile == null ? ImmutableList.of() : ImmutableList.copyOf(uploadedFile);
  }

  @Override
  public BlobKey getBlobKey(List<FileInfo> fileKeys) {
    if (fileKeys.isEmpty()) {
      throw new IllegalStateException("File should never be empty in the FE form");
    }

    FileInfo fileInfo = fileKeys.get(0);
    return blobstoreService.createGsBlobKey(fileInfo.getGsObjectName());
  }

  @Override
  public Receipt createParsedReceipt(String blobKey, String expenditureName) throws IOException {
    return new ReceiptData().extractReceiptData(blobKey, expenditureName);
  }
}

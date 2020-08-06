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

package com.google.edith.interfaces;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.common.collect.ImmutableList;
import com.google.edith.servlets.Receipt;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Operations for handling blobs. */
public interface ReceiptFileHandlerInterface {
  /** Redirects the page to a new tab and serves the blob. */
  void serveBlob(HttpServletResponse response, List<FileInfo> fileKeys) throws IOException;

  /**
   * Returns a List of BlobKey that points to the uploaded files in the HTML form or null if the
   * user didn't upload a file.
   *
   * @return ImmutableList<FileInfo> - immutable list of uploaded files from the FE form.
   */
  ImmutableList<FileInfo> getUploadedFileUrl(
      HttpServletRequest request, String formInputElementName);

  /**
   * Returns a BlobKey that points to the uploaded file.
   *
   * @return BlobKey - blobkey of the file uploaded in Blobstore.
   */
  BlobKey getBlobKey(List<FileInfo> fileKeys);

  /**
   * Returns a Parsed Receipt.
   *
   * @return Receipt - a receipt parsed from the uploaded file.
   */
  Receipt createParsedReceipt(String blobKey, String expenditureName) throws IOException;
}

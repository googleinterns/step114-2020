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
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Operations for handling blobs. */
public interface ReceiptFileHandlerInterface {
  void serveBlob(HttpServletResponse response, List<FileInfo> fileKeys) throws IOException;

  List<FileInfo> getUploadedFileUrl(HttpServletRequest request, String formInputElementName);

  BlobKey getBlobKey(List<FileInfo> fileKeys);
}

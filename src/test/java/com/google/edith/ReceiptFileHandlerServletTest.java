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

package com.google.edith;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableList;
import com.google.edith.interfaces.ReceiptFileHandlerInterface;
import com.google.edith.servlets.Item;
import com.google.edith.servlets.Receipt;
import com.google.edith.servlets.ReceiptFileHandlerServlet;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class ReceiptFileHandlerServletTest {
  private final LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(
          new LocalBlobstoreServiceTestConfig(), new LocalDatastoreServiceTestConfig());

  private ReceiptFileHandlerServlet receiptFileHandlerServlet;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    receiptFileHandlerServlet = new ReceiptFileHandlerServlet(receiptFileHandler);
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  @Mock ReceiptFileHandlerInterface receiptFileHandler;

  // If no file is uploaded, Blob is not stored and Exception is thrown.
  @Test(expected = IllegalStateException.class)
  public void doPost_ifNoFileUpload_throwException() throws IOException {
    List<FileInfo> files = Collections.emptyList();
    when(receiptFileHandler.getUploadedFileUrl(request, "receipt-file")).thenReturn(files);

    receiptFileHandlerServlet.doPost(request, response);
  }

  // Create receipt object file has been stored successfully in Blobstore.
  @Test
  public void doPost_ifFileUpload_createParsedReceipt() throws IOException {
    Date creationDate = new Date();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    List<FileInfo> files = ImmutableList.of(uploadFile);
    BlobKey returnBlobKey = new BlobKey("blob");
    Item[] items = new Item[1];
    Receipt receiptData =
        new Receipt("userId", "storeName", "date", "name", "fileUrl", 0.5f, items);
    when(request.getParameter("expense-name")).thenReturn("name");
    when(receiptFileHandler.getUploadedFileUrl(request, "receipt-file")).thenReturn(files);
    when(receiptFileHandler.getBlobKey(files)).thenReturn(returnBlobKey);
    when(receiptFileHandler.createParsedReceipt(returnBlobKey.getKeyString(), "name"))
        .thenReturn(receiptData);

    receiptFileHandlerServlet.doPost(request, response);

    verify(receiptFileHandler, times(1)).createParsedReceipt(returnBlobKey.getKeyString(), "name");
  }
}

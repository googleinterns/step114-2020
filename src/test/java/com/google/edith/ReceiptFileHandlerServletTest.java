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

import com.google.edith.servlets.ReceiptFileHandlerServlet;
import com.google.edith.services.ReceiptFileHandlerService;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mockito.MockitoAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReceiptFileHandlerServletTest {
    private final LocalServiceTestHelper testHelper = 
      new LocalServiceTestHelper(
        new LocalBlobstoreServiceTestConfig(),
        new LocalDatastoreServiceTestConfig());

  private ReceiptFileHandlerServlet receiptFileHandlerServlet;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    receiptFileHandlerServlet = new ReceiptFileHandlerServlet(receiptFileHandlerService);
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }
  
  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;
  
  @Mock
  ReceiptFileHandlerService receiptFileHandlerService;

  @Test(expected = IllegalStateException.class)
  public void throwIllegalStateException() throws IOException {
    List<FileInfo> files = Collections.emptyList();
    when(receiptFileHandlerService.getUploadedFileUrl(request, "receipt-file")).thenReturn(Optional.of(files));
    receiptFileHandlerServlet.doPost(request, response);
  }

  @Test
  public void testRedirect() throws IOException {
    Date creationDate = new Date();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    List<FileInfo> files = new ArrayList<FileInfo>();
    files.add(uploadFile);
    when(receiptFileHandlerService.getUploadedFileUrl(request, "receipt-file")).thenReturn(Optional.of(files));
    receiptFileHandlerServlet.doPost(request, response);
    verify(receiptFileHandlerService, times(1)).serveBlob(response, files);
  }
}

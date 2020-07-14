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

import com.google.edith.servlets.BlobstoreUrlServlet;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.services.ReceiptFileHandlerService;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReceiptFileHandlerServiceTest {

  private final LocalServiceTestHelper testHelper = 
      new LocalServiceTestHelper(
        new LocalBlobstoreServiceTestConfig(),
        new LocalDatastoreServiceTestConfig());

  private ReceiptFileHandlerService receiptFileHandlerService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
    receiptFileHandlerService = new ReceiptFileHandlerService(BlobstoreServiceFactory.getBlobstoreService());
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Mock
  HttpServletRequest request;

  @Test
  public void getUploadedFileUrlWhenEmpty() throws IOException {
    // Optional<List<FileInfo>> emptyList = Optional.empty();
    
    // when(receiptFileHandlerService.getUploadedFileUrl(request, "example")).thenReturn(emptyList);
    // receiptFileHandlerService.getUploadedFileUrl(request, "example");
    // verify(receiptFileHandlerService, atLeast(1)).getUploadedFileUrl(request, "example");
  }
  
  @Test
  public void getUploadedFileUrlWhenNotEmpty() throws IOException {
  }

  @Test
  public void getBlobKeyWhenEmpty() throws IOException {
  }

  @Test
  public void getBlobKeyWhenNotEmpty() throws IOException {
  }
}

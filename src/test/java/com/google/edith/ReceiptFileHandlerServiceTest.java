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

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.services.ReceiptFileHandlerService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.times;
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
    receiptFileHandlerService = new ReceiptFileHandlerService(blobstoreService);
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Mock
  BlobstoreService blobstoreService;

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Test
  public void checks_ifBlobDidNotUpload_returnsNull() throws IOException {
    Map<String, List<FileInfo>> emptyMap = Collections.emptyMap();

    when(blobstoreService.getFileInfos(request)).thenReturn(emptyMap);
    Optional<List<FileInfo>> uploadedFileInfo = receiptFileHandlerService
            .getUploadedFileUrl(request, "example");
    assertFalse(uploadedFileInfo.isPresent());
  }

  /** Checks if a file uploaded successfully in Blobstore returns BlobInfo. */
  @Test
  public void checks_ifBlobUploaded_returnsFileInfo() throws IOException {
    Date creationDate = new Date();
    List<FileInfo> files = new ArrayList<FileInfo>();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    files.add(uploadFile);
    Map<String, List<FileInfo>> fileInfos = new HashMap<>();
    fileInfos.put("fileName", files);

    when(blobstoreService.getFileInfos(request)).thenReturn(fileInfos);
    Optional<List<FileInfo>> uploadedFileInfo = receiptFileHandlerService
            .getUploadedFileUrl(request, "fileName");
    assertTrue(uploadedFileInfo.isPresent());
  }

  /** Checks if a file did not upload in Blobstore throws Exception. */
  @Test(expected = IllegalStateException.class)
  public void checks_ifFileDidNotUpload_throwsException() throws IOException {
    List<FileInfo> files = Collections.emptyList();
    receiptFileHandlerService.getBlobKey(files);
  }

  /** Checks if a file uploaded successfully in Blobstore returns url. */
  @Test
  public void checks_ifFileUploaded_returnsBlobKey() throws IOException {
    Date creationDate = new Date();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    List<FileInfo> files = new ArrayList<FileInfo>();
    files.add(uploadFile);
    BlobKey receiptKey = new BlobKey("key");
    when(blobstoreService.createGsBlobKey(uploadFile.getGsObjectName()))
            .thenReturn(receiptKey);
    BlobKey returnedKey = receiptFileHandlerService.getBlobKey(files);
    assertTrue(returnedKey.equals(receiptKey));
  }

  /** 
   * Checks if the serve method of BlobstoreService
   * is called with the right parameters.
   */
  @Test
  public void check_redirectsWithRightArguments_callsServeMethod() throws IOException {
    Date creationDate = new Date();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    List<FileInfo> files = new ArrayList<FileInfo>();
    files.add(uploadFile);
    BlobKey key = new BlobKey("key");
    when(blobstoreService.createGsBlobKey(uploadFile.getGsObjectName()))
           .thenReturn(key);
    receiptFileHandlerService.serveBlob(response, files);
    verify(blobstoreService, times(1)).serve(key, response);
  }
}

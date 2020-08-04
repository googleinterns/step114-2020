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

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.edith.services.ReceiptFileHandlerService;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class ReceiptFileHandlerServiceTest {

  private final LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(
          new LocalBlobstoreServiceTestConfig(), new LocalDatastoreServiceTestConfig());

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

  @Mock BlobstoreService blobstoreService;

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  /** Checks if a file was not uploaded in Blobstore. */
  @Test
  public void getUploadedFileUrl_ifBlobDidNotUpload_returnEmptyList() throws IOException {
    Map<String, List<FileInfo>> emptyMap = Collections.emptyMap();
    when(blobstoreService.getFileInfos(request)).thenReturn(emptyMap);

    List<FileInfo> uploadedFileInfo =
        receiptFileHandlerService.getUploadedFileUrl(request, "example");

    assertThat(uploadedFileInfo.isEmpty()).isTrue();
  }

  /** Checks if a file uploaded successfully in Blobstore returns BlobInfo. */
  @Test
  public void getUploadedFileUrl_ifBlobUploaded_returnFileInfo() throws IOException {
    Date creationDate = new Date();
    Map<String, List<FileInfo>> fileInfos =
        ImmutableMap.of(
            "fileName",
            ImmutableList.of(new FileInfo("blob", new Date(), "receipt", 0L, "hash", "edith")));
    when(blobstoreService.getFileInfos(request)).thenReturn(fileInfos);

    List<FileInfo> uploadedFileInfo =
        receiptFileHandlerService.getUploadedFileUrl(request, "fileName");

    assertThat(!uploadedFileInfo.isEmpty()).isTrue();
  }

  /** Checks if a file did not upload in Blobstore throws Exception. */
  @Test(expected = IllegalStateException.class)
  public void getBlobKey_ifFileDidNotUpload_throwException() throws IOException {
    List<FileInfo> files = ImmutableList.of();

    receiptFileHandlerService.getBlobKey(files);
  }

  /** Checks if a file uploaded successfully in Blobstore returns url. */
  @Test
  public void getBlobKey_ifFileUploaded_returnBlobKey() throws IOException {
    Date creationDate = new Date();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    List<FileInfo> files = ImmutableList.of(uploadFile);
    BlobKey receiptKey = new BlobKey("key");
    when(blobstoreService.createGsBlobKey(uploadFile.getGsObjectName())).thenReturn(receiptKey);

    BlobKey returnedKey = receiptFileHandlerService.getBlobKey(files);

    assertThat(returnedKey.equals(receiptKey)).isTrue();
  }

  /** Checks if the serve method of BlobstoreService is called with the right parameters. */
  @Test
  public void serveBlob_withCorrectParameters_callBlobstoreServiceServeMethod() throws IOException {
    Date creationDate = new Date();
    FileInfo uploadFile = new FileInfo("blob", creationDate, "receipt", 0L, "hash", "edith");
    List<FileInfo> files = ImmutableList.of(uploadFile);
    BlobKey key = new BlobKey("key");
    when(blobstoreService.createGsBlobKey(uploadFile.getGsObjectName())).thenReturn(key);

    receiptFileHandlerService.serveBlob(response, files);

    verify(blobstoreService, times(1)).serve(key, response);
  }
}

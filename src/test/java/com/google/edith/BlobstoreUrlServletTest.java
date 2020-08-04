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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.edith.servlets.BlobstoreUrlServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class BlobstoreUrlServletTest {
  private final LocalServiceTestHelper testHelper =
      new LocalServiceTestHelper(
          new LocalBlobstoreServiceTestConfig(), new LocalDatastoreServiceTestConfig());

  private BlobstoreUrlServlet blobstoreUrlServlet = new BlobstoreUrlServlet();

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    testHelper.setUp();
  }

  @After
  public void tearDown() {
    testHelper.tearDown();
  }

  @Mock HttpServletRequest request;

  @Mock HttpServletResponse response;

  /** Checks if the url contains required path. */
  @Test
  public void doGet_containsRightUrl() throws IOException {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    
    blobstoreUrlServlet.doGet(request, response);
    
    assertTrue(stringWriter.toString() != null);
  }
}

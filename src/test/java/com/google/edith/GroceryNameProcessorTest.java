package com.google.edith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
/**
@RunWith(JUnit4.class)
public class GroceryNameProcessorTest {
  private GroceryNameProcessor processor;
  private String result;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    result = "";
  }

  @Test
  public void canGetDealIfExists() throws Exception {
    GroceryNameProcessor processor = new GroceryNameProcessor(request -> {
      AnalyzeEntitiesResponse entityResponse = new AnalyzeEntitiesResponse();

      Entity entity1 = new Entity();
      entity1.setName("Coleman Farms chicken breast");
      Entity entity2 = new Entity();
      entity2.setName("chicken breast");

      entityResponse.setEntities(ImmutableList.of(entity1, entity2));

      EntityMention mention1 = new EntityMention();
      mention1.setType("PROPER");
      entity1.setMentions(ImmutableList.of(mention1));
      EntityMention mention2 = new EntityMention();
      mention2.setType("COMMON");
      entity2.setMentions(ImmutableList.of(mention2));
      return entityResponse;
    });

    Assert.assertEquals(result, "chicken breast");
  }

  @Test
  public void confusingInput() throws Exception {
    when(processor.process(Mockito.anyString())).thenReturn("red pepper");
    result = processor.process("SPCIS RED PEPPER <+");
    Assert.assertEquals(result, "red pepper");
  }

  @Test
  public void canMatch() throws Exception {
    when(processor.process(Mockito.anyString())).thenReturn("eggs");
    result = processor.process("Kirkland Farms pasture-raised eggs");
    Assert.assertEquals(result, "eggs");
  }
}
*/
/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Integration (system) tests for {@link Analyze}. */
@RunWith(JUnit4.class)
//@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class GroceryNameProcessorTest {

  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String BUCKET = PROJECT_ID;

  private ByteArrayOutputStream bout;
  private PrintStream out;

  @Before
  public void setUp() {
    bout = new ByteArrayOutputStream();
    out = new PrintStream(bout);
    System.setOut(out);
  }

  @Test
  public void analyze() throws Exception {
    GroceryNameProcessor.process("Coleman Farms chicken breast");
    String got = bout.toString();
    assertThat(got).contains("chicken breast");
  }
}

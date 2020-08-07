package com.google.edith;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.common.collect.ImmutableList;
import com.google.edith.GroceryNameProcessor.LanguageServiceClientWrapper;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;
import com.google.common.collect.ImmutableList;
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse.Builder;

@RunWith(JUnit4.class)
public class GroceryNameProcessorTest {
  private String result;

  class FakeLanguageServiceCloser implements LanguageServiceClientWrapper {
    private AnalyzeEntitiesResponse response;

    FakeLanguageServiceCloser(AnalyzeEntitiesResponse response) {
      this.response = response;
    }

    public AnalyzeEntitiesResponse analyzeEntities(AnalyzeEntitiesRequest request) {
      return response;
    }

    public void close() {}
  }

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
    result = "";
  }

  @Test
  public void canGetDealIfExists() throws Exception {
    EntityMention mention1 = EntityMention.newBuilder().setType(EntityMention.Type.COMMON).build();
    EntityMention mention2 = EntityMention.newBuilder().setType(EntityMention.Type.PROPER).build();
    Entity entity1 = Entity.newBuilder().setName("chicken breast").addMentions(mention1).build();
    Entity entity2 = Entity.newBuilder().setName("Coleman Farms").addMentions(mention2).build();
    AnalyzeEntitiesResponse entityResponse =
        AnalyzeEntitiesResponse.newBuilder()
            .addAllEntities(ImmutableList.of(entity1, entity2))
            .build();
    GroceryNameProcessor processor =
        new GroceryNameProcessor(() -> new FakeLanguageServiceCloser(entityResponse));
    result = processor.process("Coleman Farms chicken breast");
    Assert.assertEquals(result, "chicken breast");
  }
}

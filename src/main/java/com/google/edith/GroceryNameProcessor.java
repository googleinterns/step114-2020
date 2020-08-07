package com.google.edith;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.AutoCloseable;

public class GroceryNameProcessor {

  interface LanguageServiceClientProvider {
    LanguageServiceClientWrapper get() throws IOException;
  }

  interface LanguageServiceClientWrapper extends AutoCloseable {
    AnalyzeEntitiesResponse analyzeEntities(AnalyzeEntitiesRequest request);
  }

  class LanguageServiceCloser implements LanguageServiceClientWrapper {
    private LanguageServiceClient client;

    LanguageServiceCloser() throws IOException {
      client = LanguageServiceClient.create();
    }

    public AnalyzeEntitiesResponse analyzeEntities(AnalyzeEntitiesRequest request) {
      return client.analyzeEntities(request);
    }

    public void close() {
      client.close();
    }
  }

  private static LanguageServiceClientProvider clientProvider;

  GroceryNameProcessor() {
    this.clientProvider = () -> new LanguageServiceCloser();
  }

  GroceryNameProcessor(LanguageServiceClientProvider client) {
    this.clientProvider = client;
  }

  public String process(String text) throws Exception {
    List<Entity> commonEntities = new ArrayList<Entity>();
    try (LanguageServiceClientWrapper client = clientProvider.get()) {
      Document doc =
          Document.newBuilder().setContent(text.toLowerCase()).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitiesRequest request =
          AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();

      AnalyzeEntitiesResponse response = client.analyzeEntities(request);

      for (Entity entity : response.getEntitiesList()) {
        for (EntityMention mention : entity.getMentionsList()) {
          String str = String.format("%s", mention.getType());
          if (str.equals("COMMON")) {
            commonEntities.add(entity);
          }
        }
      }
    }
    if (commonEntities.size() >= 1) {
      return commonEntities.get(0).getName();
    }
    return "";
  }
}

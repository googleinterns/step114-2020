package com.google.edith;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
<<<<<<< HEAD
=======
import java.io.IOException;
>>>>>>> edc7eeb167e91ef7647bcc8fd9533ef56c3a615f
import java.util.ArrayList;
import java.util.List;

public class GroceryNameProcessor {

  private final LanguageServiceClient client;

  GroceryNameProcessor() throws IOException {
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
<<<<<<< HEAD
=======
      this.client = language;
    }
  }

  GroceryNameProcessor(LanguageServiceClient client) {
    this.client = client;
  }

  public String process(String text) {
    List<Entity> commonEntities = new ArrayList<Entity>();
    try {
>>>>>>> edc7eeb167e91ef7647bcc8fd9533ef56c3a615f
      Document doc =
          Document.newBuilder().setContent(text.toLowerCase()).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitiesRequest request =
          AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();

<<<<<<< HEAD
      AnalyzeEntitiesResponse response = language.analyzeEntities(request);
      List<Entity> commonEntities = new ArrayList<Entity>();
=======
      AnalyzeEntitiesResponse response = client.analyzeEntities(request);
>>>>>>> edc7eeb167e91ef7647bcc8fd9533ef56c3a615f

      for (Entity entity : response.getEntitiesList()) {
        for (EntityMention mention : entity.getMentionsList()) {
          String str = String.format("%s", mention.getType());
          if (str.equals("COMMON")) {
            commonEntities.add(entity);
          }
        }
      }
    } finally {
      client.close();
    }

    if (commonEntities.size() >= 1) {
      return commonEntities.get(0).getName();
    }
    return "";
  }
}

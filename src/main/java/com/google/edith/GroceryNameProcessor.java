package com.google.edith;

// Imports the Google Cloud client library
import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroceryNameProcessor {
  public String process(String text) throws Exception {
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(text.toLowerCase()).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitiesRequest request =
          AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();

      AnalyzeEntitiesResponse response = language.analyzeEntities(request);
      List<Entity> commonEntities = new ArrayList<Entity>();
      
      for (Entity entity : response.getEntitiesList()) {
        for (EntityMention mention : entity.getMentionsList()) {
          String str = String.format("%s", mention.getType());
          if (str.equals("COMMON")) {
            commonEntities.add(entity);
          }
        }
      }
      if (commonEntities.size() >= 1) {
        return commonEntities.get(0).getName();
      }
      return "";
    }
  }
}
package com.google.edith.servlets;

import com.google.cloud.documentai.v1beta2.Document;
import com.google.cloud.documentai.v1beta2.DocumentUnderstandingServiceClient;
import com.google.cloud.documentai.v1beta2.GcsSource;
import com.google.cloud.documentai.v1beta2.InputConfig;
import com.google.cloud.documentai.v1beta2.ProcessDocumentRequest;
import java.io.IOException;

public class ExtractReceipt {

  public static void extractReceipt() throws IOException {
    String projectId = "edith-step";
    String location = "us";
    String inputGcsUri = "gs://edith-receipts/AAANsUmjLAOYQp4Rn9XphEflkVYntq1WQX4m9oczEGqXTn4m7vce4b3d02B0Qe1jYgF2IGJRHTSN6E3u4FSREZrQgbI.SvrR-Q83SYV-TNgy";
    extractReceipt(projectId, location, inputGcsUri);
  }

  public static void extractReceipt(String projectId, String location, String inputGcsUri)
      throws IOException {
    // Initialize client that will be used to send requests. This client only needs to be created
    // once, and can be reused for multiple requests. After completing all of your requests, call
    // the "close" method on the client to safely clean up any remaining background resources.
    try (DocumentUnderstandingServiceClient client = DocumentUnderstandingServiceClient.create()) {
      // Configure the request for processing a single document
      String parent = String.format("projects/%s/locations/%s", projectId, location);

      GcsSource uri = GcsSource.newBuilder().setUri(inputGcsUri).build();

      InputConfig config =
          InputConfig.newBuilder().setGcsSource(uri).setMimeType("application/pdf").build();

      ProcessDocumentRequest request =
          ProcessDocumentRequest.newBuilder().setParent(parent).setInputConfig(config).build();

      // Recognizes text entities in the PDF document
      Document response = client.processDocument(request);

      // Get all of the document text as one big string
      String text = response.getText();

      for (String item: text.split("\\r?\\n")) {
        if (item.endsWith(" B")) {
          String[] itemText = item.split("\\s+");
          if (itemText.length > 1 && itemText[itemText.length - 2].matches("[-+]?[0-9]*\\.?[0-9]+") && !itemText[0].matches("[-+]?[0-9]*\\.?[0-9]+")) {
            System.out.println(item);
          }
        }
      }
    }
  }
}

package com.google.edith.servlets;

import com.google.cloud.documentai.v1beta2.Document;
import com.google.cloud.documentai.v1beta2.DocumentUnderstandingServiceClient;
import com.google.cloud.documentai.v1beta2.GcsSource;
import com.google.cloud.documentai.v1beta2.InputConfig;
import com.google.cloud.documentai.v1beta2.ProcessDocumentRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ExtractReceipt {

  public List<Map<String, String>> extractReceipt() throws IOException {
    String projectId = "edith-step";
    String location = "us";
    String blobKey = ReceiptFileHandlerServlet.getFileBlobKey();
    // For local testing. As blobstore API does not store in GCS in local environment.
    String inputGcsUri = "gs://edith-receipts/AAANsUmjLAOYQp4Rn9XphEflkVYntq1WQX4m9oczEGqXTn4m7vce4b3d02B0Qe1jYgF2IGJRHTSN6E3u4FSREZrQgbI.SvrR-Q83SYV-TNgy";
    return extractReceipt(projectId, location, inputGcsUri);
  }

  private List<Map<String, String>> extractReceipt(String projectId, String location, String inputGcsUri)
      throws IOException {

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
      List<Map<String, String>> items = new ArrayList<Map<String, String>>();

      for (String item: text.split("\\r?\\n")) {
        if (item.endsWith(" B")) {
          String[] itemText = item.split("\\s+");
          if (itemText.length > 1 && itemText[itemText.length - 2].matches("[-+]?[0-9]*\\.?[0-9]+") && !itemText[0].matches("[-+]?[0-9]*\\.?[0-9]+")) {
            items.add(processItem(itemText));
          }
        }
      }
      return items;
    }
  }

  private Map<String, String> processItem (String[] itemText) {
    int itemPriceIndex = itemText.length - 2;
    int index = 0;
    String itemName = "";
    while (index < itemPriceIndex) {
      itemName = itemName + " " + itemText[index++];
    }
    Map<String, String> itemFields= new HashMap<String, String>();
    itemFields.put("itemPrice", itemText[itemPriceIndex]);
    itemFields.put("itemName", itemName.trim());
    return itemFields;
  }
}

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

package com.google.edith.services;

import com.google.cloud.documentai.v1beta2.Document;
import com.google.cloud.documentai.v1beta2.DocumentUnderstandingServiceClient;
import com.google.cloud.documentai.v1beta2.GcsSource;
import com.google.cloud.documentai.v1beta2.InputConfig;
import com.google.cloud.documentai.v1beta2.ProcessDocumentRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Splitter;
import com.google.edith.interfaces.ExtractReceiptInterface;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uses Document AI API to parse pdf receipt file stored in Google Cloud Storage Bucket named
 * edith-step.
 */
public final class ExtractReceiptService implements ExtractReceiptInterface {
  private final DocumentUnderstandingServiceClient client;
  private static final String INPUT_GCS_URI = 
      "gs://edith-receipts/AAANsUmjLAOYQp4Rn9XphEflkVYntq1WQX4m9oczEGqXTn4m7vce4b3d02B0Qe1jYgF2IGJRHTSN6E3u4FSREZrQgbI.SvrR-Q83SYV-TNgy";
  public ExtractReceiptService(DocumentUnderstandingServiceClient client) {
    this.client = client;
  }

  @Override
  public ImmutableList<ImmutableMap<String, String>> extractReceipt(String blobKey) throws IOException {
    String projectId = "edith-step";
    String location = "us";
    // For local testing. As blobstore API does not store in GCS in local environment.
    String parsedText = extractReceipt(projectId, location, INPUT_GCS_URI);
    return createItems(parsedText);
  }

  /**
   * This method is because of the shortfall of Document AI API, Natural Language Processing and
   * Cloud Vision API. All of these APIs were not optimal to parse the receipt as they could not
   * scan purchased items correctly.
   *
   * @return List<Map<String, String>> - a list of maps of item name as key and price as value
   */
  private ImmutableList<ImmutableMap<String, String>> createItems(String parsedText) {
    List<ImmutableMap<String, String>> items = new ArrayList<ImmutableMap<String, String>>();
    // Split the string on new lines.
    for (String item : Splitter.onPattern("\\r?\\n").splitToList(parsedText)) {
      // It is more specific to Kroger as the price ends with B.
      // TODO(prashantneu@) make the algorithm more general considering receipts from other stores.
      if (item.endsWith(" B")) {
        List<String> itemText = Splitter.onPattern("\\s+").splitToList(item);
        // Only consider the parsed text if it has item descriptions and price.
        if (itemText.size() > 1
            && itemText.get(itemText.size() - 2).matches("[-+]?[0-9]*\\.?[0-9]+")
            && !itemText.get(0).matches("[-+]?[0-9]*\\.?[0-9]+")) {
          items.add(processItem(itemText));
        }
      }
    }
    return ImmutableList.copyOf(items);
  }

  /**
   * Calls Document AI API and parses the pdf file as a string.
   *
   * @return String - string representation of the content of the receipt pdf file.
   */
  private String extractReceipt(String projectId, String location, String inputGcsUri)
      throws IOException {

    String parent = String.format("projects/%s/locations/%s", projectId, location);
    GcsSource uri = GcsSource.newBuilder().setUri(inputGcsUri).build();

    InputConfig config =
        InputConfig.newBuilder().setGcsSource(uri).setMimeType("application/pdf").build();

    ProcessDocumentRequest request =
        ProcessDocumentRequest.newBuilder().setParent(parent).setInputConfig(config).build();

    Document response = client.processDocument(request);

    return response.getText();
  }

  /**
   * Given a item description string, creates a map where item name is key and price is value
   *
   * @return Map<String, String> - a map where item name as key and price as value
   */
  private ImmutableMap<String, String> processItem(List<String> itemText) {
    int itemPriceIndex = itemText.size() - 2;
    int index = 0;
    // Combines the splitted text into a single item description.
    StringBuilder itemName = new StringBuilder();
    while (index < itemPriceIndex) {
      itemName.append(" ");
      itemName.append(itemText.get(index++));
    }
    Map<String, String> itemFields = new HashMap<String, String>();
    itemFields.put("itemPrice", itemText.get(itemPriceIndex));
    itemFields.put("itemName", itemName.toString().trim());
    return ImmutableMap.copyOf(itemFields);
  }
}

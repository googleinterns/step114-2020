package com.google.edith.servlets;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.cloud.documentai.v1beta2.DocumentUnderstandingServiceClient;
import com.google.edith.interfaces.ExtractReceiptInterface;
import com.google.edith.services.ExtractReceiptService;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/** Converts parsed receipt file string to receipt and item objects. */
public final class ReceiptData {

  private ExtractReceiptInterface extractReceiptImplementation;
  private static final String UNKNOWN_STORE = "UNKNOWN_STORE";
  private static final String UNKNOWN_DATE = "UNKNOWN_DATE";

  public ReceiptData() throws IOException {
    this.extractReceiptImplementation =
        new ExtractReceiptService(DocumentUnderstandingServiceClient.create());
  }

  public ReceiptData(ExtractReceiptInterface extractReceiptImplementation) {
    this.extractReceiptImplementation = extractReceiptImplementation;
  }

  /**
   * Creates receipt object.
   *
   * @param blobKey - string representation of the blob in GCS bucket.
   * @param expenditureName - name of the rceipt obtained from FE form.
   * @return Receipt - receipt object created from the provided info.
   */
  public Receipt extractReceiptData(String blobKey, String expenditureName) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();

    ImmutableList<ImmutableMap<String, String>> items = extractReceiptImplementation.extractReceipt(blobKey);
    Item[] parsedItems = createReceiptItems(user, items);
    Receipt parsedReceipt = createReceipt(blobKey, user, parsedItems, expenditureName);
    return parsedReceipt;
  }

  /**
   * Creates receipt object.
   *
   * @param blobKey - string representation of the blob in GCS bucket.
   * @param user - user who is logged in.
   * @param items - Array of items parsed from receipt pdf file.
   * @param expenditureName - name of the rceipt obtained from FE form.
   * @return Receipt - receipt object created from the provided info.
   */
  private Receipt createReceipt(String blobKey, User user, Item[] items, String expenditureName) {
    Receipt userReceipt =
        new Receipt(
            user.getUserId(),
            UNKNOWN_STORE,
            UNKNOWN_DATE,
            expenditureName,
            blobKey,
            0.0f,
            items);
    return userReceipt;
  }

  /**
   * Creates Array of Item objects from list of item description .
   *
   * @param user - user who is logged in.
   * @param extractedData - List of item description.
   * @return Item[] - array of item objects created from the list of item description.
   */
  private Item[] createReceiptItems(User user, ImmutableList<ImmutableMap<String, String>> extractedData) {
    int index = 0;
    int totalItems = extractedData.size();
    Item[] items = new Item[totalItems];
    while (index < totalItems) {
      Map<String, String> itemFields = extractedData.get(index);
      Item receiptItem =
          Item.builder()
              .setUserId(user.getUserId())
              .setName(itemFields.get("itemName"))
              .setPrice(Float.parseFloat(itemFields.get("itemPrice")))
              .setQuantity(0)
              .setCategory("unknown category")
              .setExpiration("unknown date")
              .setDate("unknown")
              .build();

      items[index++] = receiptItem;
    }
    return items;
  }
}

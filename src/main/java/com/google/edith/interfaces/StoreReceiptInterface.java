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

package com.google.edith.interfaces;

import com.google.edith.servlets.Receipt;
import java.io.BufferedReader;
import java.io.IOException;

/** Operations for handling parsing receipts. */
public interface StoreReceiptInterface {

  /**
   * Stores Receipt and Item entities in datastore
   *
   * @param receipt - object which holds info of parsed file.
   */
  void storeEntites(Receipt receipt);

  /**
   * Parses the form submitted by user which contains information of the parsed receipt and creates
   * a Receipt object from the JSON string.
   *
   * @param request - request which contains the form body.
   * @return Receipt - Receipt object created from the JSON string.
   */
  Receipt parseReceiptFromForm(BufferedReader bufferedReader) throws IOException;
}

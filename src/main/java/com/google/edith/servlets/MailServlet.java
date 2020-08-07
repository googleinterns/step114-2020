/**
 * Copyright 2016 Google Inc.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.edith.servlets;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/notifications")
public class MailServlet extends HttpServlet {

  private final boolean mockSend;

  MailServlet() {
    this.mockSend = false;
  }

  public MailServlet(boolean mockSend) {
    this.mockSend = mockSend;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    StringBuilder body = new StringBuilder();
    String subject = "";

    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line).append('\n');
      }
    }
    String receiptData = stringBuilder.toString();
    JsonParser parser = new JsonParser();
    JsonArray items = parser.parse(receiptData).getAsJsonArray();

    subject = "Restock Alert";
    body.append("Hello! You're running low on a few items. You might want to restock:");
    body.append("/n");
    for (int i = 0; i < items.size(); i++) {
      JsonObject item = items.get(i).getAsJsonObject();
      body.append(item.get("name").getAsString());
      body.append("/n");
    }
    if (mockSend) {
      response.getWriter().println(body.toString());
    } else {
      sendSimpleMail(subject, body.toString());
    }
    response.sendRedirect("/");
  }

  private void sendSimpleMail(String subject, String message) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("livseibert@google.com", "Admin"));
      msg.addRecipient(
          Message.RecipientType.TO,
          new InternetAddress(
              UserServiceFactory.getUserService().getCurrentUser().getUserId(), "User"));
      msg.setSubject(subject);
      msg.setText(message);
      Transport.send(msg);
    } catch (AddressException e) {
      System.out.println(e.getMessage());
    } catch (MessagingException e) {
      System.out.println(e.getMessage());
    } catch (UnsupportedEncodingException e) {
      System.out.println(e.getMessage());
    }
  }
}

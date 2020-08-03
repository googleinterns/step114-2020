 
/**
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.appengine.mail;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/notifications")
public class MailServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String type = request.getParameter("type");
    StringBuilder body = new StringBuilder();
    String subject;

    if (type.equals(expirationQuery)) {
      String json = request.getParameter("body");
      subject = "Restock Alert";
      JsonArray groceryItems = json.getAsJsonArray();
      body.append("Hello! You're running low on a few items. You might want to restock:");
      body.append("/n");
      for (int i = 0; i < groceryItems.length; i++) {
        body.append(groceryItems[i]);
        body.append("/n");
      }
    }

    sendSimpleMail(subject, body.toString());
    response.sendRedirect("/");
  }

  private void sendSimpleMail(String subject, String message) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress("livseibert@google.com", "Example.com Admin"));
      msg.addRecipient(Message.RecipientType.TO,
                       new InternetAddress("livseibert@google.com", "Mr. User"));
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

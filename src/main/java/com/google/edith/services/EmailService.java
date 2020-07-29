package com.google.edith;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class EmailService {
  private final CreateEmail createEmail = new CreateEmail();
  private final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
  private static final String FROM_EMAIL = "livseibert@google.com";
  
  public Message sendEmail() {
    LoginService loginService = new LoginService();
    if (loginService.checkUserLoggedIn()) {
      UserInfo user = loginService.createUserInfo();
      String userEmail = user.getEmail();
      String userId = user.getUserId();
      Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();
      
      String subject = "test email";
      String body = "test body";
      MimeMessage mimeMessage = createEmail.createEmail(userEmail, FROM_EMAIL, subject, body);
      Message messageOut = createEmail.sendMessage(service, userId, mimeMessage);
    }
  }
}

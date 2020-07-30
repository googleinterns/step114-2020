package com.google.edith;

import com.google.edith.services.LoginService;
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
import javax.mail.MessagingException;
import com.google.api.services.gmail.model.Message;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import com.google.api.services.gmail.model.Message;
import javax.mail.internet.MimeMessage;
import com.google.edith.servlets.UserInfo;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import java.io.ByteArrayOutputStream;
import org.apache.commons.codec.binary.Base64;
import java.security.GeneralSecurityException;

public class EmailService {
  private final UserService userService = UserServiceFactory.getUserService();

  private NetHttpTransport HTTP_TRANSPORT;
  private static final String APPLICATION_NAME = "Testing Gmail API";
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String FROM_EMAIL = "livseibert@google.com";
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, delete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
  private static final String CREDENTIALS_FILE_PATH = "oauth-key.json";
  
  public void sendEmail(String subject, String body) throws IOException, GeneralSecurityException, MessagingException {
    if (userService.isUserLoggedIn()) {
      User currentUser = userService.getCurrentUser();
      // String recipient = currentUser.getEmail();
      String recipient = "livseibert@google.com";
      String userId = currentUser.getUserId();
      HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
      Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
        .setApplicationName(APPLICATION_NAME)
        .build();
    
      MimeMessage mimeMessage = createEmail(recipient, FROM_EMAIL, subject, body);
      Message messageOut = sendMessage(service, userId, mimeMessage);
    }
  }

  /**
   * Creates an authorized Credential object.
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
    // Load client secrets.
    //InputStream in = EmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    InputStream in = getClass().getClassLoader().getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

    /**
  * Create a MimeMessage using the parameters provided.
  *
  * @param to email address of the receiver
  * @param from email address of the sender, the mailbox account
  * @param subject subject of the email
  * @param bodyText body text of the email
  * @return the MimeMessage to be used to send email
  * @throws MessagingException
  */
  private static MimeMessage createEmail(String to,
      String from,
      String subject,
      String bodyText) throws MessagingException {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    MimeMessage email = new MimeMessage(session);

    email.setFrom(new InternetAddress(from));
    email.addRecipient(javax.mail.Message.RecipientType.TO,
      new InternetAddress(to));
    email.setSubject(subject);
    email.setText(bodyText);
    return email;
  }

    /**
   * Send an email from the user's mailbox to its recipient.
   *
   * @param service Authorized Gmail API instance.
   * @param userId User's email address. The special value "me"
   * can be used to indicate the authenticated user.
   * @param emailContent Email to be sent.
   * @return The sent message
   * @throws MessagingException
   * @throws IOException
   */
  private static Message sendMessage(Gmail service,
        String userId,
        MimeMessage emailContent)
        throws MessagingException, IOException {
    Message message = createMessageWithEmail(emailContent);
    message = service.users().messages().send(userId, message).execute();

    System.out.println("Message id: " + message.getId());
    System.out.println(message.toPrettyString());
    return message;
  }

  /**
   * Create a message from an email.
   *
   * @param emailContent Email to be set to raw of message
   * @return a message containing a base64url encoded email
   * @throws IOException
   * @throws MessagingException
   */
  public static Message createMessageWithEmail(MimeMessage emailContent)
    throws MessagingException, IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    emailContent.writeTo(buffer);
    byte[] bytes = buffer.toByteArray();
    String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
    Message message = new Message();
    message.setRaw(encodedEmail);
    return message;
  }
}

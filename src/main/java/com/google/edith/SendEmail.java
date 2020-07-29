package com.google.edith;

import java.io.IOException;
import javax.mail.internet.MimeMessage;

public class SendEmail {
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
  public static MimeMessage createEmail(String to,
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
    public static Message sendMessage(Gmail service,
        String userId,
        MimeMessage emailContent)
        throws MessagingException, IOException {
      Message message = createMessageWithEmail(emailContent);
      message = service.users().messages().send(userId, message).execute();

      System.out.println("Message id: " + message.getId());
      System.out.println(message.toPrettyString());
      return message;
    }
}

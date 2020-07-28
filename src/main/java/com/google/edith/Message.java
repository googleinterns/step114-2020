package com.google.edith;

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

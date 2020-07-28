package com.google.edith;
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

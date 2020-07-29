package com.google.edith.servlets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.edith.EmailService;
import javax.mail.MessagingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/notifications")
public class NotificationsServlet extends HttpServlet {
  private EmailService emailService = new EmailService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      String subject = "test subject";
      String body = "test body";
      emailService.sendEmail(subject, body);
    } catch (GeneralSecurityException e) {
      System.out.println(e.getMessage());
    } catch (MessagingException e) {
      System.out.println(e.getMessage());
    }
  }
}

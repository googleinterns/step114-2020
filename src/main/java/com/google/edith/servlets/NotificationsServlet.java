package com.google.edith.servlets;

import com.google.edith.EmailService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/notifications")
public class NotificationsServlet extends HttpServlet {
  private EmailService emailService = new EmailService();
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    emailService.sendEmail();
  }
}

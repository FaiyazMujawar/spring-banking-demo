package com.tsystems.banking.services.mail;

public interface MailService {
  void sendHtmlMail(String receiver, String subject, String body);
}

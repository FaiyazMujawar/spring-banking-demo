package com.tsystems.banking.services.mail;

import com.tsystems.banking.exceptions.MailingException;

public interface MailService {
  void sendHtmlMail(String receiver, String subject, String body)
    throws MailingException;
}

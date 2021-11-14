package com.tsystems.banking.services.mail;

import com.tsystems.banking.config.AppConfig;
import com.tsystems.banking.exceptions.MailingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImplementation implements MailService {
  private final JavaMailSender mailSender;
  private final AppConfig appConfig;

  /**
   * @param mailSender
   * @param appConfig
   */
  @Autowired
  public MailServiceImplementation(
    JavaMailSender mailSender,
    AppConfig appConfig
  ) {
    this.mailSender = mailSender;
    this.appConfig = appConfig;
  }

  @Override
  @Async
  public void sendHtmlMail(String receiver, String subject, String body)
    throws MailingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

    try {
      messageHelper.setTo(receiver);
      messageHelper.setSubject(subject);
      messageHelper.setText(body, true);
      messageHelper.setFrom(appConfig.getMailerEmail());

      mailSender.send(mimeMessage);
    } catch (MessagingException e) {
      e.printStackTrace();
      throw new MailingException(e.getLocalizedMessage());
    }
  }
}

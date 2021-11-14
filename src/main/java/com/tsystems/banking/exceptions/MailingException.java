package com.tsystems.banking.exceptions;

public class MailingException extends RuntimeException {

  /**
   * @param message
   */
  public MailingException(String message) {
    super(message);
  }
}

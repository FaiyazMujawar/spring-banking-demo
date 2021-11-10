package com.tsystems.banking.exceptions;

public class AccountNotFoundException extends RuntimeException {

  /**
   * @param message
   */
  public AccountNotFoundException(String message) {
    super(message);
  }
}

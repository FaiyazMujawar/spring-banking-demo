package com.tsystems.banking.exceptions;

public class UserNotFoundException extends RuntimeException {

  /**
   * @param message
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}

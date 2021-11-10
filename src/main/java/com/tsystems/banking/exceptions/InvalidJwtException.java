package com.tsystems.banking.exceptions;

public class InvalidJwtException extends RuntimeException {

  /**
   * @param message
   */
  public InvalidJwtException(String message) {
    super(message);
  }
}

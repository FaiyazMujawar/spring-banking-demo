package com.tsystems.banking.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final HttpStatus exceptionType;

  public ApiException(HttpStatus exceptionType, String message) {
    super(message);
    this.exceptionType = exceptionType;
  }

  /**
   * @return the exceptionType
   */
  public HttpStatus getExceptionType() {
    return exceptionType;
  }
}

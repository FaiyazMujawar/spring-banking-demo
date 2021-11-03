package com.tsystems.banking.api.response;

import java.util.Date;

public class ErrorResponse {
  private final String result;
  private final String code;
  private final Date timestamp;
  private final Object error;

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * @return the exceptionType
   */
  public String getCode() {
    return code;
  }

  /**
   * @return the error
   */
  public Object getError() {
    return error;
  }

  /**
   * @param result
   * @param code
   * @param error
   */
  public ErrorResponse(String code, Object error) {
    this.result = "FAILURE";
    this.code = code.toUpperCase().replaceAll(" ", "_");
    this.error = error;
    this.timestamp = new Date();
  }
}

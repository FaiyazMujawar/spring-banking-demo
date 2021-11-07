package com.tsystems.banking.api.response;

import java.util.Date;

public class SuccessfulResponse {
  private final Date timestamp;
  private final String result;
  private final Object response;

  /**
   * @param response
   */
  public SuccessfulResponse(Object response) {
    this.result = "SUCCESS";
    this.response = response;
    this.timestamp = new Date();
  }

  /**
   * @return the timestamp
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * @return the response
   */
  public Object getResponse() {
    return response;
  }
}

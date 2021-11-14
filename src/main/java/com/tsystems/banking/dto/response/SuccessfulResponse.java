package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel(description = "Successful response model")
public class SuccessfulResponse {
  @ApiModelProperty(
    name = "timestamp",
    notes = "Time at which the response was sent"
  )
  private final Date timestamp;

  @ApiModelProperty(
    name = "result",
    notes = "Result of the request",
    value = "SUCCESS"
  )
  private final String result;

  /**
   * @param response
   */
  public SuccessfulResponse() {
    this.result = "SUCCESS";
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
}

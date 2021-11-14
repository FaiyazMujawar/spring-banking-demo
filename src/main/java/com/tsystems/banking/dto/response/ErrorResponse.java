package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.ZonedDateTime;

@ApiModel(description = "Error Response model")
public class ErrorResponse {
  @ApiModelProperty(name = "timestamp", notes = "Time at sending the response")
  private final String timestamp;

  @ApiModelProperty(
    name = "result",
    notes = "Result of the request",
    value = "FAILURE"
  )
  private final String result;

  @ApiModelProperty(name = "code", notes = "HTTP error code phrase")
  private final String code;

  @ApiModelProperty(
    name = "error",
    notes = "Actual error. A map of error messages"
  )
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
  public String getTimestamp() {
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
    this.timestamp = ZonedDateTime.now().toString();
  }
}

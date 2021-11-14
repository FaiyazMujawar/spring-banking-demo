package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Successful Update balance model")
public class UpdateBalanceResponse extends SuccessfulResponse {
  @ApiModelProperty(name = "message", notes = "Account balance update message")
  private final String message;

  /**
   * @param message
   */
  public UpdateBalanceResponse(String message) {
    this.message = message;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }
}

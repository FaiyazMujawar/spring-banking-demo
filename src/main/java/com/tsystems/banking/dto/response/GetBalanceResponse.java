package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Get Response Model")
public class GetBalanceResponse extends SuccessfulResponse {
  @ApiModelProperty(name = "balance", notes = "The balance in the account")
  private final Double balance;

  /**
   * @param balance
   */
  public GetBalanceResponse(Double balance) {
    this.balance = balance;
  }

  /**
   * @return the balance
   */
  public Double getBalance() {
    return balance;
  }
}

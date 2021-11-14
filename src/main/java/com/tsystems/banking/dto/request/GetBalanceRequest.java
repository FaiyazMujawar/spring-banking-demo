package com.tsystems.banking.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Request body to get account balance")
public class GetBalanceRequest {
  @NotNull(message = "Account number is required")
  @ApiModelProperty(name = "AccountId", notes = "Account number")
  private Long accountId;

  public GetBalanceRequest() {}

  /**
   * @return the accountId
   */
  public Long getAccountId() {
    return accountId;
  }

  /**
   * @param accountId the accountId to set
   */
  public void setAccountId(Long accountId) {
    this.accountId = accountId;
  }
}

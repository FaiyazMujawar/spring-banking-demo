package com.tsystems.banking.api.request;

import javax.validation.constraints.NotNull;

public class GetBalanceInput {
  @NotNull(message = "Account number is required")
  private Long accountId;

  public GetBalanceInput() {}

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

package com.tsystems.banking.api.request;

public class GetBalanceInput {
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

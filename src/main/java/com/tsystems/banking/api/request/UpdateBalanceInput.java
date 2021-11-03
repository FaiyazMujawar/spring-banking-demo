package com.tsystems.banking.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UpdateBalanceInput {
  @NotNull(message = "Account number is required")
  private Long accountId;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be greater that 0")
  private Double amount;

  public UpdateBalanceInput() {}

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

  /**
   * @return the amount
   */
  public Double getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(Double amount) {
    this.amount = amount;
  }
}

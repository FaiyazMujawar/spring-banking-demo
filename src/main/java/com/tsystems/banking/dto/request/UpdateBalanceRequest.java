package com.tsystems.banking.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@ApiModel(description = "Update Balance Request")
public class UpdateBalanceRequest {
  @NotNull(message = "Account number is required")
  @ApiModelProperty(
    name = "accountId",
    notes = "Account number of the account",
    required = true
  )
  private Long accountId;

  @NotNull(message = "Amount is required")
  @Positive(message = "Amount must be greater that 0")
  @ApiModelProperty(
    name = "amount",
    notes = "Amount to deposit/withdraw",
    required = true
  )
  private Double amount;

  public UpdateBalanceRequest() {}

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

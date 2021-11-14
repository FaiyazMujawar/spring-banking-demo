package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Successful register response")
public class RegisterResponse extends SuccessfulResponse {
  @ApiModelProperty(
    name = "username",
    notes = "Auto-generated username for the user"
  )
  private final String username;

  @ApiModelProperty(
    name = "accountNumber",
    notes = "Account number of the newly created account"
  )
  private final Long accountNumber;

  @ApiModelProperty(name = "accessToken", notes = "Signed access token")
  private final String accessToken;

  @ApiModelProperty(
    name = "refreshToken",
    notes = "Refresh token to renew expired access token"
  )
  private final String refreshToken;

  /**
   * @return the accessToken
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * @return the refreshToken
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * @return the accountNumber
   */
  public Long getAccountNumber() {
    return accountNumber;
  }

  /**
   * @param response
   * @param username
   * @param accountNumber
   * @param refreshToken
   */
  public RegisterResponse(
    String username,
    Long accountNumber,
    String accessToken,
    String refreshToken
  ) {
    this.username = username;
    this.accountNumber = accountNumber;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }
}

package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Successful Login Response")
public class LoginResponse extends SuccessfulResponse {
  @ApiModelProperty(name = "accessToken", notes = "Signed access token")
  private String accessToken;

  @ApiModelProperty(
    name = "refreshToken",
    notes = "Refresh token to renew the expired access token"
  )
  private String refreshToken;

  /**
   * @param accessToken
   * @param refreshToken
   */
  public LoginResponse(String accessToken, String refreshToken) {
    this.setAccessToken(accessToken);
    this.setRefreshToken(refreshToken);
  }

  /**
   * @return the accessToken
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * @param accessToken the accessToken to set
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /**
   * @return the refreshToken
   */
  public String getRefreshToken() {
    return refreshToken;
  }

  /**
   * @param refreshToken the refreshToken to set
   */
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}

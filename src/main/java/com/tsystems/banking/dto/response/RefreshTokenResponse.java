package com.tsystems.banking.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Successful refresh token response")
public class RefreshTokenResponse extends SuccessfulResponse {
  @ApiModelProperty(name = "Renewed access token")
  private final String accessToken;

  /**
   * @param response
   * @param refreshToken
   */
  public RefreshTokenResponse(String refreshToken) {
    this.accessToken = refreshToken;
  }

  /**
   * @return the refreshToken
   */
  public String getAccessToken() {
    return accessToken;
  }
}

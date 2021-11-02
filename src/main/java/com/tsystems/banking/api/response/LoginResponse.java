package com.tsystems.banking.api.response;

public class LoginResponse {
  private String accessToken;
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
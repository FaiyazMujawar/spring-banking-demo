package com.tsystems.banking.dto;

import com.tsystems.banking.dto.response.ApiHealthResponse;
import com.tsystems.banking.dto.response.GetBalanceResponse;
import com.tsystems.banking.dto.response.LoginResponse;
import com.tsystems.banking.dto.response.RefreshTokenResponse;
import com.tsystems.banking.dto.response.RegisterResponse;
import com.tsystems.banking.dto.response.UpdateBalanceResponse;

public class DtoMapper {

  public static GetBalanceResponse toGetBalanceResponse(Double balance) {
    return new GetBalanceResponse(balance);
  }

  public static UpdateBalanceResponse toUpdateBalanceDto(String message) {
    return new UpdateBalanceResponse(message);
  }

  public static RegisterResponse toRegisterResponse(
    String username,
    Long accountNumber,
    String accessToken,
    String refreshToken
  ) {
    return new RegisterResponse(
      username,
      accountNumber,
      accessToken,
      refreshToken
    );
  }

  public static RefreshTokenResponse toRefreshTokenResponse(
    String accessToken
  ) {
    return new RefreshTokenResponse(accessToken);
  }

  public static LoginResponse toLoginDto(
    String accessToken,
    String refreshToken
  ) {
    return new LoginResponse(accessToken, refreshToken);
  }

  public static ApiHealthResponse toApiHealthResponse(
    String message,
    String repositoryURL
  ) {
    return new ApiHealthResponse(message, repositoryURL);
  }
}

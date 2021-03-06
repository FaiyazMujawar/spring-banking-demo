package com.tsystems.banking.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.banking.exceptions.InvalidJwtException;
import java.text.DecimalFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
  private static ObjectMapper objectMapper = null;

  private static String getMail(String name, String body) {
    return (
      "Dear ," +
      name +
      "<br />" +
      "<br />" +
      body +
      "<br />" +
      "<br />" +
      "Thank you," +
      "<br />" +
      "Bank"
    );
  }

  public static Authentication getAuthenticatedUser() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static String getLowBalanceAlertMail(
    String name,
    Long accountNumber,
    Double minimumBalance
  ) {
    String body =
      "This email is to alert you that your account with account number <b>%d</b> has less balance than the minimum amount of <b>%s</b>. We urge you to please maintain the minimum balance";

    return String.format(
      getMail(name, body),
      accountNumber,
      getDoubleWithPrecision(minimumBalance, 2)
    );
  }

  public static String getAmountDepositMail(
    String name,
    Long accountNumber,
    Double amount
  ) {
    String body =
      "This email is to inform you that your account with account number <b>%d</b> has been credited with amount <b>%s</b>.";

    return String.format(
      getMail(name, body),
      accountNumber,
      getDoubleWithPrecision(amount, 2)
    );
  }

  public static String getAmountWithdrawMail(
    String name,
    Long accountNumber,
    Double amount
  ) {
    String body =
      "This email is to inform you that your account with account number <b>%d</b> has been debited with amount <b>%s</b>.";

    return String.format(
      getMail(name, body),
      accountNumber,
      getDoubleWithPrecision(amount, 2)
    );
  }

  public static String getDoubleWithPrecision(Double number, int precision) {
    String pattern = "#.";
    for (int i = 0; i < precision; i++) {
      pattern += "#";
    }

    return new DecimalFormat(pattern).format(number);
  }

  public static String getTokenFromAuthHeader(String authHeader)
    throws InvalidJwtException {
    if (authHeader == null || authHeader.isBlank()) {
      throw new InvalidJwtException(Constants.ACCESS_TOKEN_REQUIRED_ERROR);
    }

    if (!authHeader.startsWith("Bearer ")) {
      throw new InvalidJwtException(Constants.AUTH_HEADER_MALFORMED_ERROR);
    }

    return authHeader.substring(Constants.TOKEN_PREFIX.length());
  }

  public static String generateUsernameFromEmail(String email) {
    return email.split("@")[0] + (int) (Math.random() * 100);
  }

  public static ObjectMapper getObjectMapper() {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }

    return objectMapper;
  }
}

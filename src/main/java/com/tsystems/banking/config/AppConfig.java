package com.tsystems.banking.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * AppConfig
 */

public class AppConfig {
  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Value("${jwt.expiration}")
  private String JWT_EXPIRATION_TIME;

  @Value("${spring.mail.username}")
  private String MAILER_ADDRESS;

  @Value("${account.minimum_balance}")
  private Double MINIMUM_ACCOUNT_BALANCE;

  public String getJwtSecret() {
    return JWT_SECRET;
  }

  public Long getJwtExpirationTimeInMillis() {
    Long time = Long.parseLong(JWT_EXPIRATION_TIME.replaceAll("[^\\d+]", ""));

    switch (JWT_EXPIRATION_TIME.replaceAll("\\d+", "")) {
      case "mi":
        // minutes
        return time * 60 * 1000;
      case "h":
        // hours
        return time * 60 * 60 * 1000;
      case "d":
        // days
        return time * 24 * 60 * 60 * 1000;
      case "mo":
        // months
        return time * 30 * 24 * 60 * 60 * 1000;
      case "yr":
        // years
        return time * 365 * 24 * 60 * 60 * 1000;
    }

    return time * 1000;
  }

  public String getMailerEmail() {
    return MAILER_ADDRESS;
  }

  public Double getMinimumAccountBalance() {
    return MINIMUM_ACCOUNT_BALANCE;
  }
}

package com.tsystems.banking.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * AppConfig
 */

public class AppConfig {
  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Value("${jwt.expiration}")
  private int JWT_EXPIRATION_TIME_IN_HRS;

  public String getJwtSecret() {
    return JWT_SECRET;
  }

  public int getJwtExpirationTimeInHrs() {
    return JWT_EXPIRATION_TIME_IN_HRS;
  }
}

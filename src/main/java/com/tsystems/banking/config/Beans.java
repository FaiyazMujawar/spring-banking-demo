package com.tsystems.banking.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.tsystems.banking.services.jwt.JwtService;
import com.tsystems.banking.services.jwt.JwtServiceImplementation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableAsync
public class Beans {
  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Bean
  public BCryptPasswordEncoder getBCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtService jwtService() {
    return new JwtServiceImplementation(this.hashAlgorithm());
  }

  @Bean
  public Algorithm hashAlgorithm() {
    return Algorithm.HMAC256(JWT_SECRET.getBytes());
  }

  @Bean
  public AppConfig appConfig() {
    return new AppConfig();
  }
}

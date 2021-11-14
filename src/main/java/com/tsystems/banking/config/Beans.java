package com.tsystems.banking.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.tsystems.banking.services.jwt.JwtService;
import com.tsystems.banking.services.jwt.JwtServiceImplementation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableAsync
@EnableOpenApi
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

  @Bean
  public Docket bankingAPI() {
    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(getApiInfo())
      .select()
      .apis(RequestHandlerSelectors.basePackage("com.tsystems.banking"))
      .build();
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
      .title("Banking REST API Documentation")
      .description("Documentation for REST API for a demo banking application")
      .license("Apache 2.0")
      .build();
  }
}

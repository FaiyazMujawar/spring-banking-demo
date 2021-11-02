package com.tsystems.banking.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.banking.api.request.UsernamePasswordAuthRequest;
import com.tsystems.banking.api.response.LoginResponse;
import com.tsystems.banking.config.AppConfig;
import com.tsystems.banking.services.jwt.JwtService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {
  @Value("${jwt.expiration}")
  private int JWT_EXPIRATION_TIME_IN_HRS;

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final AppConfig appConfig;

  /**
   * @param authenticationManager
   * @param jwtService
   */
  public JwtAuthFilter(
    AuthenticationManager authenticationManager,
    JwtService jwtService,
    AppConfig appConfig
  ) {
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.appConfig = appConfig;

    super.setFilterProcessesUrl("/api/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(
    HttpServletRequest request,
    HttpServletResponse response
  )
    throws AuthenticationException {
    try {
      // Extracting username/password from request to
      // UsernamePasswordAuthRequest class
      UsernamePasswordAuthRequest authRequest = new ObjectMapper()
      .readValue(request.getInputStream(), UsernamePasswordAuthRequest.class);

      // Creating authentication token from username/password
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        authRequest.getUsername(),
        authRequest.getPassword()
      );

      // Authenticating the user
      return authenticationManager.authenticate(authToken);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  )
    throws IOException, ServletException {
    // User class from springframework, not custom defined
    User user = (User) authResult.getPrincipal();

    int expirationTimeInHrs = appConfig.getJwtExpirationTimeInHrs();
    String accessToken = jwtService.signToken(
      user.getUsername(),
      request.getLocalName(),
      Optional.empty(),
      Optional.of(expirationTimeInHrs)
    );

    String refreshToken = jwtService.signToken(
      user.getUsername(),
      request.getLocalName(),
      Optional.empty(),
      Optional.empty()
    );

    new ObjectMapper()
    .writeValue(
        response.getOutputStream(),
        new LoginResponse(accessToken, refreshToken)
      );
  }
}
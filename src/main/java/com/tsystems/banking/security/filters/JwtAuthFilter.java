package com.tsystems.banking.security.filters;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.config.AppConfig;
import com.tsystems.banking.dto.DtoMapper;
import com.tsystems.banking.dto.request.LoginRequest;
import com.tsystems.banking.dto.response.ErrorResponse;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.misc.Utils;
import com.tsystems.banking.services.jwt.JwtService;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {
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
    LoginRequest authRequest = null;

    try {
      // Extracting username/password from request to LoginRequest class
      authRequest =
        Utils
          .getObjectMapper()
          .readValue(request.getInputStream(), LoginRequest.class);
    } catch (IOException e) {
      response.setContentType(APPLICATION_JSON_VALUE);
      response.setStatus(BAD_REQUEST.value());

      try {
        Utils
          .getObjectMapper()
          .writeValue(
            response.getOutputStream(),
            new ErrorResponse(
              BAD_REQUEST.getReasonPhrase(),
              Map.ofEntries(
                Map.entry("message", Constants.REQUEST_BODY_UNREADABLE_ERROR)
              )
            )
          );
      } catch (IOException ioException) {}

      return null;
    }

    // Creating authentication token from username/password
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      authRequest.getUsername(),
      authRequest.getPassword()
    );

    // Authenticating the user
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain chain,
    Authentication authResult
  )
    throws IOException, ServletException {
    // User class from spring framework, not custom defined
    User user = (User) authResult.getPrincipal();

    Long expirationTimeInMillis = appConfig.getJwtExpirationTimeInMillis();
    String accessToken = jwtService.signToken(
      user.getUsername(),
      request.getLocalName(),
      Optional.empty(),
      Optional.of(expirationTimeInMillis)
    );

    String refreshToken = jwtService.signToken(
      user.getUsername(),
      request.getLocalName(),
      Optional.empty(),
      Optional.empty()
    );

    response.setContentType(APPLICATION_JSON_VALUE);

    Utils
      .getObjectMapper()
      .writeValue(
        response.getOutputStream(),
        DtoMapper.toLoginDto(accessToken, refreshToken)
      );
  }

  @Override
  protected void unsuccessfulAuthentication(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException failed
  )
    throws IOException, ServletException {
    response.setStatus(FORBIDDEN.value());
    response.setContentType(APPLICATION_JSON_VALUE);

    Map<String, String> errors = Map.ofEntries(
      Map.entry("message", failed.getLocalizedMessage())
    );

    Utils
      .getObjectMapper()
      .writeValue(
        response.getOutputStream(),
        new ErrorResponse(FORBIDDEN.getReasonPhrase(), errors)
      );
  }
}

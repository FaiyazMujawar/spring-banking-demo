package com.tsystems.banking.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.banking.api.response.ErrorResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthExceptionHandler implements AuthenticationFailureHandler {
  private final String APPLICATION_JSON = "application/json";

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception
  )
    throws IOException, ServletException {
    System.err.println("Called with: " + exception.getMessage());

    response.setContentType(APPLICATION_JSON);
    response.setStatus(HttpStatus.FORBIDDEN.value());

    new ObjectMapper()
    .writeValue(
        response.getOutputStream(),
        new ErrorResponse(
          HttpStatus.FORBIDDEN.getReasonPhrase(),
          exception.getMessage()
        )
      );
  }
}

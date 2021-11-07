package com.tsystems.banking.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.banking.api.response.ErrorResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthExceptionHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException exception
  )
    throws IOException, ServletException {
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(FORBIDDEN.value());

    System.err.println("\nErrorResponse:\t" + exception.getLocalizedMessage());

    new ObjectMapper()
    .writeValue(
        response.getOutputStream(),
        new ErrorResponse(
          FORBIDDEN.getReasonPhrase(),
          exception.getLocalizedMessage()
        )
      );
  }
}

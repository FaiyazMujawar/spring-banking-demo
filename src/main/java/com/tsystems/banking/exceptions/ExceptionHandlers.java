package com.tsystems.banking.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.tsystems.banking.api.response.ErrorResponse;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
  private final String APPLICATION_JSON = "application/json";

  @ExceptionHandler(value = { ApiException.class })
  public ResponseEntity<ErrorResponse> handleApiException(
    ApiException apiException,
    HttpServletResponse response
  ) {
    response.setContentType(APPLICATION_JSON);

    return ResponseEntity
      .status(apiException.getExceptionType().value())
      .body(
        new ErrorResponse(
          apiException.getExceptionType().getReasonPhrase(),
          apiException.getMessage()
        )
      );
  }

  @ExceptionHandler(value = { MethodArgumentNotValidException.class })
  public ResponseEntity<ErrorResponse> handleValidationException(
    MethodArgumentNotValidException methodArgumentNotValidException,
    HttpServletResponse response
  ) {
    response.setContentType(APPLICATION_JSON);

    Map<String, String> errors = new HashMap<>();

    methodArgumentNotValidException
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .forEach(
        error -> {
          errors.put(error.getField(), error.getDefaultMessage());
        }
      );

    return ResponseEntity
      .status(BAD_REQUEST)
      .body(new ErrorResponse(BAD_REQUEST.getReasonPhrase(), errors));
  }

  @ExceptionHandler(value = { Exception.class })
  public ResponseEntity<?> handleException(
    Exception exception,
    HttpServletResponse response
  ) {
    response.setContentType(APPLICATION_JSON);

    return ResponseEntity
      .status(INTERNAL_SERVER_ERROR)
      .body(
        new ErrorResponse(
          INTERNAL_SERVER_ERROR.getReasonPhrase(),
          exception.getMessage()
        )
      );
  }
}

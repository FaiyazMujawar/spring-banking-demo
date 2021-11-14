package com.tsystems.banking.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.dto.response.ErrorResponse;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

  @ExceptionHandler(value = { ApiException.class })
  public ResponseEntity<ErrorResponse> handleApiException(
    ApiException apiException,
    HttpServletResponse response
  ) {
    response.setContentType(APPLICATION_JSON_VALUE);

    return ResponseEntity
      .status(apiException.getExceptionType().value())
      .body(
        new ErrorResponse(
          apiException.getExceptionType().getReasonPhrase(),
          Map.ofEntries(
            Map.entry("message", apiException.getLocalizedMessage())
          )
        )
      );
  }

  @ExceptionHandler(value = { HttpMessageNotReadableException.class })
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException() {
    Map<String, String> error = Map.ofEntries(
      Map.entry("message", "Request body not readable")
    );

    return ResponseEntity
      .badRequest()
      .body(new ErrorResponse(BAD_REQUEST.getReasonPhrase(), error));
  }

  @ExceptionHandler(value = { MethodArgumentNotValidException.class })
  public ResponseEntity<ErrorResponse> handleValidationException(
    MethodArgumentNotValidException methodArgumentNotValidException,
    HttpServletResponse response
  ) {
    response.setContentType(APPLICATION_JSON_VALUE);

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
  public ResponseEntity<ErrorResponse> handleException(
    Exception exception,
    HttpServletResponse response
  ) {
    exception.printStackTrace();
    response.setContentType(APPLICATION_JSON_VALUE);

    return ResponseEntity
      .status(INTERNAL_SERVER_ERROR)
      .body(
        new ErrorResponse(
          INTERNAL_SERVER_ERROR.getReasonPhrase(),
          exception.getLocalizedMessage()
        )
      );
  }
}

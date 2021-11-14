package com.tsystems.banking.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.dto.DtoMapper;
import com.tsystems.banking.dto.response.ApiHealthResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/health")
public class ApiHealthController {

  public ApiHealthController() {}

  @GetMapping
  @ApiOperation(
    value = "Get API Health",
    notes = "Controller for getting API health"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Ok",
        response = ApiHealthResponse.class
      ),
    }
  )
  public ResponseEntity<ApiHealthResponse> apiHealth(
    HttpServletResponse response
  ) {
    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        DtoMapper.toApiHealthResponse(
          "API is up and running",
          "https://gitlab.com/faiyazmujawar/banking"
        )
      );
  }
}

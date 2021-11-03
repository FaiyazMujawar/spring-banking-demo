package com.tsystems.banking.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.api.response.SuccessfulResponse;
import java.util.HashMap;
import java.util.Map;
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
  public ResponseEntity<SuccessfulResponse> apiHealth(
    HttpServletResponse response
  ) {
    Map<String, String> message = new HashMap<>();
    message.put("message", "API is up and running");
    message.put("gitlabURL", "https://gitlab.com/faiyazmujawar/banking");

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity.ok().body(new SuccessfulResponse(message));
  }
}

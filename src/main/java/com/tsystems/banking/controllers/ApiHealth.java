package com.tsystems.banking.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/health")
public class ApiHealth {

  public ApiHealth() {}

  @GetMapping
  public ResponseEntity<?> apiHealth() {
    return ResponseEntity.ok().build();
  }
}

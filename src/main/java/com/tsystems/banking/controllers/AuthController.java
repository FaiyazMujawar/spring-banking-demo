package com.tsystems.banking.controllers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tsystems.banking.api.request.RegisterInput;
import com.tsystems.banking.api.response.SuccessfulResponse;
import com.tsystems.banking.config.AppConfig;
import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.jwt.JwtService;
import com.tsystems.banking.services.user.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
  private final UserService userService;
  private final JwtService jwtService;
  private final AccountService accountService;
  private final BCryptPasswordEncoder passwordEncoder;
  private final AppConfig appConfig;

  /**
   * @param userService
   * @param jwtService
   * @param accountService
   * @param passwordEncoder
   */
  @Autowired
  public AuthController(
    UserService userService,
    JwtService jwtService,
    AccountService accountService,
    BCryptPasswordEncoder passwordEncoder,
    AppConfig appConfig
  ) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
    this.accountService = accountService;
    this.appConfig = appConfig;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<SuccessfulResponse> registerUser(
    @RequestBody @Valid RegisterInput registerInput,
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    // Generate a username from email
    String username =
      registerInput.getEmail().split("@")[0] + (int) (Math.random() * 100);

    if (userService.existsByEmail(registerInput.getEmail())) {
      throw new ApiException(BAD_REQUEST, "Email already in use");
    }

    // Create a new user
    User user = userService.createUser(
      new User(
        registerInput.getFirstName(),
        registerInput.getLastName(),
        username,
        registerInput.getEmail(),
        passwordEncoder.encode(registerInput.getPassword()),
        registerInput.getContact()
      )
    );

    accountService.createAccount(new Account(user.getId(), 0.0));

    // Generate accessToken & refreshToken for the user
    String accessToken = jwtService.signToken(
      user.getUsername(),
      request.getLocalName(),
      Optional.empty(),
      Optional.of(appConfig.getJwtExpirationTimeInMillis())
    );

    String refreshToken = jwtService.signToken(
      user.getUsername(),
      request.getLocalName(),
      Optional.empty(),
      Optional.empty()
    );

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity.ok().body(new SuccessfulResponse(tokens));
  }

  @PostMapping(path = "/refresh")
  public ResponseEntity<SuccessfulResponse> refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  )
    throws Exception {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    DecodedJWT decodedJWT = jwtService.verifyToken(authorizationHeader);
    String username = decodedJWT.getSubject();

    String accessToken = jwtService.signToken(
      username,
      request.getLocalName(),
      Optional.empty(),
      Optional.of(appConfig.getJwtExpirationTimeInMillis())
    );

    Map<String, String> token = new HashMap<>();
    token.put("accessToken", accessToken);

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity.ok().body(new SuccessfulResponse(token));
  }
}
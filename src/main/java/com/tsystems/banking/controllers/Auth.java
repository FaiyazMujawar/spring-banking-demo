package com.tsystems.banking.controllers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.tsystems.banking.api.request.RegisterRequest;
import com.tsystems.banking.api.response.LoginResponse;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.jwt.JwtService;
import com.tsystems.banking.services.user.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class Auth {
  private final UserService userService;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder;

  @Value("${jwt.expiration}")
  private int JWT_EXPIRATION_TIME_IN_HRS;

  /**
   * @param userService
   * @param jwtService
   * @param passwordEncoder
   */
  @Autowired
  public Auth(
    UserService userService,
    JwtService jwtService,
    BCryptPasswordEncoder passwordEncoder
  ) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping(path = "/register")
  public ResponseEntity<LoginResponse> registerUser(
    @RequestBody RegisterRequest request,
    HttpServletRequest req
  ) {
    // TODO: Implement data validation

    // Generate a username from email
    String username =
      request.getEmail().split("@")[0] + (int) (Math.random() * 100);

    // Create a new user
    User user = userService.createUser(
      new User(
        request.getFirstName(),
        request.getLastName(),
        username,
        request.getEmail(),
        passwordEncoder.encode(request.getPassword()),
        request.getContact()
      )
    );

    // TODO: Implement account creation

    // Generate accessToken & refreshToken for the user
    String accessToken = jwtService.signToken(
      user.getUsername(),
      req.getLocalName(),
      Optional.empty(),
      Optional.of(JWT_EXPIRATION_TIME_IN_HRS)
    );

    String refreshToken = jwtService.signToken(
      user.getUsername(),
      req.getLocalName(),
      Optional.empty(),
      Optional.empty()
    );

    return ResponseEntity
      .ok()
      .body(new LoginResponse(accessToken, refreshToken));
  }

  @PostMapping(path = "/refresh")
  public ResponseEntity<?> refreshToken(HttpServletRequest request)
    throws Exception {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    //FIXME: Send proper error response here
    DecodedJWT decodedJWT = jwtService.verifyToken(authorizationHeader);
    String username = decodedJWT.getSubject();

    String accessToken = jwtService.signToken(
      username,
      request.getLocalName(),
      Optional.empty(),
      Optional.of(JWT_EXPIRATION_TIME_IN_HRS)
    );

    Map<String, String> token = new HashMap<>();
    token.put("accessToken", accessToken);

    return ResponseEntity.ok().body(token);
  }
}
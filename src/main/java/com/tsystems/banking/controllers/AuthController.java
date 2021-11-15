package com.tsystems.banking.controllers;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.config.AppConfig;
import com.tsystems.banking.dto.DtoMapper;
import com.tsystems.banking.dto.request.RegisterRequest;
import com.tsystems.banking.dto.response.ErrorResponse;
import com.tsystems.banking.dto.response.RefreshTokenResponse;
import com.tsystems.banking.dto.response.RegisterResponse;
import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.misc.Utils;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.jwt.JwtService;
import com.tsystems.banking.services.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
  @ApiOperation(
    value = "Register user",
    notes = "Controller for registering user on the API"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Ok",
        response = RegisterResponse.class
      ),
      @ApiResponse(
        code = 400,
        message = "Bad Request",
        response = ErrorResponse.class
      ),
    }
  )
  public ResponseEntity<RegisterResponse> registerUser(
    @ApiParam(
      required = true,
      value = "User details for registering"
    ) @RequestBody @Valid RegisterRequest registerRequest,
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    String email = registerRequest.getEmail();
    String username = Utils.generateUsernameFromEmail(email);
    User user = null;

    if (userService.existsByEmail(email)) {
      if (registerRequest.getIsExistingUser()) {
        user = userService.findByEmail(email);
      } else {
        throw new ApiException(BAD_REQUEST, Constants.EMAIL_IN_USE_ERROR);
      }
    } else {
      user =
        userService.createUser(
          new User(
            registerRequest.getFirstName(),
            registerRequest.getLastName(),
            username,
            email,
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getContact()
          )
        );
    }

    Account account = accountService.createAccount(
      new Account(user.getId(), 0.0)
    );

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

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        DtoMapper.toRegisterResponse(
          username,
          account.getId(),
          accessToken,
          refreshToken
        )
      );
  }

  @PostMapping(path = "/refresh")
  @ApiOperation(
    value = "Refresh Access token",
    notes = "Controller for refreshing expired access token"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Ok",
        response = RegisterResponse.class
      ),
      @ApiResponse(
        code = 400,
        message = "Bad Request",
        response = ErrorResponse.class
      ),
    }
  )
  public ResponseEntity<RefreshTokenResponse> refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  )
    throws Exception {
    String token = null;

    try {
      token = Utils.getTokenFromAuthHeader(request.getHeader(AUTHORIZATION));

      jwtService.verifyToken(token);
    } catch (Exception e) {
      throw new ApiException(FORBIDDEN, e.getLocalizedMessage());
    }

    String username = jwtService.getSubjectFromToken(token);

    try {
      String accessToken = jwtService.signToken(
        username,
        request.getLocalName(),
        Optional.empty(),
        Optional.of(appConfig.getJwtExpirationTimeInMillis())
      );

      response.setContentType(APPLICATION_JSON_VALUE);
      return ResponseEntity
        .ok()
        .body(DtoMapper.toRefreshTokenResponse(accessToken));
    } catch (Exception e) {
      throw new ApiException(INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
  }
}

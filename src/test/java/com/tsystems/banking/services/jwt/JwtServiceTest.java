package com.tsystems.banking.services.jwt;

import static org.junit.jupiter.api.Assertions.fail;

import com.auth0.jwt.algorithms.Algorithm;
import com.tsystems.banking.exceptions.InvalidJwtException;
import com.tsystems.banking.misc.Constants;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JwtServiceTest {
  private JwtService jwtService;

  private String token =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYXlhem11amF3YXI5MzMiLCJpc3MiOiJsb2NhbGhvc3QiLCJleHAiOjE2NjkyMDAyODQsImlhdCI6MTYzNzY2NDI4NH0.chOIbjSre6wM-afQTRCtuiWXYQdYIKvXRoViBSxmP_s";

  private String expiredToken =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYXlhem11amF3YXI5MzMiLCJpc3MiOiJsb2NhbGhvc3QiLCJleHAiOjE2Mzc2NDc3MDksImlhdCI6MTYzNzY0NzcwOX0.gzAaKMCBPjyuumCsh2N1EApRQwee5PIJdzIHM-a5V5k";

  private String invalidToken = "token";

  @BeforeEach
  public void setup() {
    jwtService = new JwtServiceImplementation(Algorithm.HMAC256("secret"));
  }

  @Nested
  @DisplayName("Testing verifyToken()")
  public class testVerifyToken {

    @Test
    @DisplayName("Should return true when token is valid and non-expired")
    public void shouldReturnTrue() {
      try {
        Assertions.assertThat(jwtService.verifyToken(token)).isTrue();
      } catch (Exception e) {
        fail("Should return false, but threw " + e.getMessage());
      }
    }

    @Test
    @DisplayName("Should return false when token is invalid")
    public void shouldThrowWhenInvalid() {
      Assertions
        .assertThatThrownBy(() -> jwtService.verifyToken(invalidToken))
        .isInstanceOf(InvalidJwtException.class)
        .hasMessage(Constants.JWT_AUTH_ERROR);
    }

    @Test
    @DisplayName("Should return false when token has expired")
    public void shouldThrowOnExpiry() {
      try {
        Assertions
          .assertThatThrownBy(() -> jwtService.verifyToken(expiredToken))
          .isInstanceOf(InvalidJwtException.class)
          .hasMessageContaining("expired");
      } catch (Exception e) {
        fail("Should return false, but threw " + e.getMessage());
      }
    }

    @Test
    @DisplayName(
      "Should IllegalArgumentException when passed argument is null or blank"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> jwtService.verifyToken(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_TOKEN_ERROR);

      Assertions
        .assertThatThrownBy(() -> jwtService.verifyToken(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_TOKEN_ERROR);

      Assertions
        .assertThatThrownBy(() -> jwtService.verifyToken(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_TOKEN_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing getSubjectFromToken()")
  public class testGetSubjectFromToken {

    @Test
    @DisplayName("Should return subject when token is valid & non-expired")
    public void shouldReturnSubject() {
      Assertions
        .assertThat(jwtService.getSubjectFromToken(token))
        .isEqualTo("fayazmujawar933");
    }

    @Test
    @DisplayName("Should throw error when token is expired")
    public void shouldThrowOnExpiry() {
      Assertions
        .assertThatThrownBy(() -> jwtService.getSubjectFromToken(expiredToken))
        .isInstanceOf(InvalidJwtException.class)
        .hasMessageContaining("expired");
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is null or blank"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> jwtService.getSubjectFromToken(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_TOKEN_ERROR);

      Assertions
        .assertThatThrownBy(() -> jwtService.getSubjectFromToken(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_TOKEN_ERROR);

      Assertions
        .assertThatThrownBy(() -> jwtService.getSubjectFromToken(" "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_TOKEN_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing signToken()")
  public class testSignToken {

    @Test
    @DisplayName("Should return token when data passed is valid")
    public void shouldReturnToken() {
      Assertions
        .assertThat(
          jwtService.signToken(
            "subject",
            "issuer",
            Optional.empty(),
            Optional.empty()
          )
        )
        .isNotNull();
    }

    @Test
    @DisplayName("Should return token with expiration time")
    public void shouldReturnExpiringToken() {
      // Create a token that expires instantly
      String expiringToken = jwtService.signToken(
        "subject",
        "issuer",
        Optional.empty(),
        Optional.of(1L)
      );

      try {
        // Introduce a delay so that token expires
        Thread.sleep(1000L);

        // Verify that token actually expired
        Assertions
          .assertThatThrownBy(() -> jwtService.verifyToken(expiringToken))
          .isInstanceOf(InvalidJwtException.class)
          .hasMessageContaining("expired");
      } catch (InterruptedException e) {}
    }

    @Test
    @DisplayName("Should return token with claims")
    public void shouldReturnTokenWithClaims() {
      Map<String, Object> claims = Map.ofEntries(Map.entry("claim", "claim"));

      String tokenWithClaims = jwtService.signToken(
        "subject",
        "issuer",
        Optional.of(claims),
        Optional.empty()
      );

      Assertions
        .assertThat(jwtService.getClaimsFromToken(tokenWithClaims))
        .containsKeys("claim");
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed args are invalid"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(
          () ->
            jwtService.signToken(
              null,
              "issuer",
              Optional.empty(),
              Optional.empty()
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_SUBJECT_ERROR);

      Assertions
        .assertThatThrownBy(
          () ->
            jwtService.signToken(
              "subject",
              null,
              Optional.empty(),
              Optional.empty()
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_ISSUER_ERROR);

      Assertions
        .assertThatThrownBy(
          () ->
            jwtService.signToken(
              "subject",
              "",
              Optional.empty(),
              Optional.empty()
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_ISSUER_ERROR);

      Assertions
        .assertThatThrownBy(
          () ->
            jwtService.signToken(
              "subject",
              "issuer",
              Optional.empty(),
              Optional.of(0L)
            )
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_EXPIRATION_TIME_ERROR);
    }
  }
}

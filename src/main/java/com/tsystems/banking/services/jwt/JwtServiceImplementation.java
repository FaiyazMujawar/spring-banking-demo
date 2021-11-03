package com.tsystems.banking.services.jwt;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tsystems.banking.exceptions.ApiException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImplementation implements JwtService {
  private final Algorithm algorithm;
  private final String JWT_PREFIX = "Bearer ";

  /**
   * @param algorithm
   */
  public JwtServiceImplementation(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public String signToken(
    Object subject,
    String issuer,
    Optional<Map<String, Object>> claims,
    Optional<Long> expirationTimeInMillis
  ) {
    Builder builder = JWT
      .create()
      .withSubject(subject.toString())
      .withIssuedAt(new Date(System.currentTimeMillis()))
      .withIssuer(issuer);

    if (expirationTimeInMillis.isPresent()) {
      builder.withExpiresAt(
        new Date(System.currentTimeMillis() + expirationTimeInMillis.get())
      );
    }

    if (claims.isPresent()) {
      builder.withPayload(claims.get());
    }

    try {
      return builder.sign(algorithm);
    } catch (JWTCreationException e) {
      throw new ApiException(INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @Override
  public DecodedJWT verifyToken(String authorizationHeader) throws Exception {
    if (authorizationHeader == null) {
      throw new ApiException(FORBIDDEN, "Access token is required");
    }

    if (!authorizationHeader.startsWith(JWT_PREFIX)) {
      throw new ApiException(FORBIDDEN, "Access token is malformed");
    }

    try {
      return JWT
        .require(algorithm)
        .build()
        .verify(authorizationHeader.substring(JWT_PREFIX.length()));
    } catch (JWTVerificationException e) {
      throw new ApiException(FORBIDDEN, e.getMessage());
    }
  }
}

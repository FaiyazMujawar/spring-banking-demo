package com.tsystems.banking.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.tsystems.banking.exceptions.InvalidJwtException;
import com.tsystems.banking.misc.Constants;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImplementation implements JwtService {
  private final Algorithm algorithm;
  private final JWTVerifier jwtVerifier;

  /**
   * @param algorithm
   */
  @Autowired
  public JwtServiceImplementation(Algorithm algorithm) {
    this.algorithm = algorithm;
    this.jwtVerifier = JWT.require(algorithm).build();
  }

  @Override
  public String signToken(
    Object subject,
    String issuer,
    Optional<Map<String, Object>> claims,
    Optional<Long> expirationTimeInMillis
  ) {
    if (subject == null) {
      throw new IllegalArgumentException(Constants.INVALID_SUBJECT_ERROR);
    }

    if (issuer == null || issuer.isBlank()) {
      throw new IllegalArgumentException(Constants.INVALID_ISSUER_ERROR);
    }

    Builder builder = JWT
      .create()
      .withSubject(subject.toString())
      .withIssuedAt(new Date(System.currentTimeMillis()))
      .withIssuer(issuer);

    if (expirationTimeInMillis.isPresent()) {
      if (expirationTimeInMillis.get() <= 0) {
        throw new IllegalArgumentException(
          Constants.INVALID_EXPIRATION_TIME_ERROR
        );
      }

      builder.withExpiresAt(
        new Date(System.currentTimeMillis() + expirationTimeInMillis.get())
      );
    }

    if (claims.isPresent()) {
      builder.withPayload(claims.get());
    }

    return builder.sign(algorithm);
  }

  @Override
  public Boolean verifyToken(String token)
    throws InvalidJwtException, IllegalArgumentException {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException(Constants.INVALID_TOKEN_ERROR);
    }

    try {
      return jwtVerifier.verify(token) != null;
    } catch (TokenExpiredException e) {
      throw new InvalidJwtException(e.getLocalizedMessage());
    } catch (Exception e) {
      throw new InvalidJwtException(Constants.JWT_AUTH_ERROR);
    }
  }

  @Override
  public String getSubjectFromToken(String token)
    throws IllegalArgumentException, InvalidJwtException {
    if (token == null || token.isBlank()) {
      throw new IllegalArgumentException(Constants.INVALID_TOKEN_ERROR);
    }

    try {
      return jwtVerifier.verify(token).getSubject();
    } catch (TokenExpiredException e) {
      throw new InvalidJwtException(e.getLocalizedMessage());
    } catch (Exception e) {
      throw new InvalidJwtException(Constants.JWT_AUTH_ERROR);
    }
  }

  @Override
  public Map<String, Claim> getClaimsFromToken(String token) {
    return jwtVerifier.verify(token).getClaims();
  }
}

package com.tsystems.banking.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImplementation implements JwtService {
  @Value("${jwt.secret}")
  private String JWT_SECRET;

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
    Optional<Integer> expirationTimeInHrs
  ) {
    Builder builder = JWT
      .create()
      .withSubject(subject.toString())
      .withIssuer(issuer);

    if (expirationTimeInHrs.isPresent()) {
      builder.withExpiresAt(
        new Date(
          System.currentTimeMillis() +
          (expirationTimeInHrs.get() * 60 * 60 * 1000)
        )
      );
    }

    if (claims.isPresent()) {
      builder.withPayload(claims.get());
    }

    return builder.sign(algorithm);
  }

  @Override
  public DecodedJWT verifyToken(String authorizationHeader) throws Exception {
    if (
      authorizationHeader == null || !authorizationHeader.startsWith(JWT_PREFIX)
    ) {
      throw new Exception("JWT malformed");
    }

    return JWT
      .require(algorithm)
      .build()
      .verify(authorizationHeader.substring(JWT_PREFIX.length()));
  }
}

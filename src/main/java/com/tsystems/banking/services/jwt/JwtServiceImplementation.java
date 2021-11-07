package com.tsystems.banking.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
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

    return builder.sign(algorithm);
  }

  @Override
  public Boolean verifyToken(String token) throws Exception {
    return jwtVerifier.verify(token) != null;
  }

  @Override
  public String getSubjectFromToken(String token) {
    return jwtVerifier.verify(token).getSubject();
  }

  @Override
  public Map<String, Claim> getClaimsFromToken(String token) {
    return jwtVerifier.verify(token).getClaims();
  }
}

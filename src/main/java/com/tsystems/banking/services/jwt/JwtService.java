package com.tsystems.banking.services.jwt;

import com.auth0.jwt.interfaces.Claim;
import java.util.Map;
import java.util.Optional;

public interface JwtService {
  String signToken(
    Object subject,
    String issuer,
    Optional<Map<String, Object>> claims,
    Optional<Long> expirationTimeInMillis
  );

  Boolean verifyToken(String authorizationHeader) throws Exception;

  String getSubjectFromToken(String token);

  Map<String, Claim> getClaimsFromToken(String token);
}

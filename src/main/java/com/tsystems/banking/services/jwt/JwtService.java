package com.tsystems.banking.services.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.tsystems.banking.exceptions.InvalidJwtException;
import java.util.Map;
import java.util.Optional;

public interface JwtService {
  String signToken(
    Object subject,
    String issuer,
    Optional<Map<String, Object>> claims,
    Optional<Long> expirationTimeInMillis
  )
    throws IllegalArgumentException;

  Boolean verifyToken(String token)
    throws IllegalArgumentException, InvalidJwtException;

  String getSubjectFromToken(String token)
    throws IllegalArgumentException, InvalidJwtException;

  Map<String, Claim> getClaimsFromToken(String token);
}

package com.tsystems.banking.services.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Map;
import java.util.Optional;

public interface JwtService {
  String signToken(
    Object subject,
    String issuer,
    Optional<Map<String, Object>> claims,
    Optional<Integer> expirationTimeInHrs
  );

  DecodedJWT verifyToken(String authorizationHeader) throws Exception;
}

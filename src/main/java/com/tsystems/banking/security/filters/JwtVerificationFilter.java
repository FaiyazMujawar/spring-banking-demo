package com.tsystems.banking.security.filters;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsystems.banking.services.jwt.JwtService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtVerificationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private String APPLICATION_JSON = "application/json";

  /**
   * @param jwtService
   */
  public JwtVerificationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  )
    throws ServletException, IOException {
    String endpoint = request.getServletPath();

    // If endpoint is open, do nothing, pass to next filter in chain
    if (endpoint.matches("/api/auth/.*|/login|/api/health")) {
      filterChain.doFilter(request, response);
    } else {
      String authorizationHeader = request.getHeader(AUTHORIZATION);

      try {
        DecodedJWT token = jwtService.verifyToken(authorizationHeader);

        String username = token.getSubject();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          username,
          null,
          authorities
        );

        // Set the verified user into security context
        SecurityContextHolder
          .getContext()
          .setAuthentication(authenticationToken);

        // Pass to next filter in chain
        filterChain.doFilter(request, response);
      } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON);
        Map<String, Object> errors = new HashMap<>();

        errors.put("error", e.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), errors);
      }
    }
  }
}

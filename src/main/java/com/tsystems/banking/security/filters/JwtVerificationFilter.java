package com.tsystems.banking.security.filters;

import static com.tsystems.banking.misc.Utils.getTokenFromAuthHeader;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.dto.response.ErrorResponse;
import com.tsystems.banking.misc.Utils;
import com.tsystems.banking.services.jwt.JwtService;
import java.io.IOException;
import java.util.ArrayList;
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
    if (
      endpoint.matches("/api/auth.*|/api/health.*") ||
      !endpoint.startsWith("/api")
    ) {
      filterChain.doFilter(request, response);
    } else {
      try {
        String token = getTokenFromAuthHeader(request.getHeader(AUTHORIZATION));

        jwtService.verifyToken(token);

        String username = jwtService.getSubjectFromToken(token);
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
        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        Map<String, String> errors = Map.ofEntries(
          Map.entry("message", e.getLocalizedMessage())
        );

        ErrorResponse errorResponse = new ErrorResponse(
          FORBIDDEN.getReasonPhrase(),
          errors
        );
        Utils
          .getObjectMapper()
          .writeValue(response.getOutputStream(), errorResponse);
      }
    }
  }
}

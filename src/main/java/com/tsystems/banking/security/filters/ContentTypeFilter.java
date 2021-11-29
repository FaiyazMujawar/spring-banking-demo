package com.tsystems.banking.security.filters;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.dto.response.ErrorResponse;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.misc.Utils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

public class ContentTypeFilter extends OncePerRequestFilter {
  private List<String> emptyBodyRoutes = List.of("/api/health");

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  )
    throws ServletException, IOException {
    String contentType = request.getContentType();

    if (request.getMethod().equals(GET.name())) {
      if (
        request.getInputStream().isFinished() &&
        emptyBodyRoutes.contains(request.getRequestURI())
      ) {
        filterChain.doFilter(request, response);
        return;
      }

      if (APPLICATION_JSON_VALUE.equals(contentType)) {
        filterChain.doFilter(request, response);
        return;
      }
    } else {
      if (APPLICATION_JSON_VALUE.equals(contentType)) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    Map<String, String> error = Map.ofEntries(
      Map.entry("message", Constants.INVALID_REQUEST_BODY_ERROR)
    );

    response.setContentType(APPLICATION_JSON_VALUE);
    Utils
      .getObjectMapper()
      .writeValue(
        response.getOutputStream(),
        new ErrorResponse(BAD_REQUEST.getReasonPhrase(), error)
      );
  }
}

package com.tsystems.banking.misc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

  public static Authentication getAuthenticatedUser() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}

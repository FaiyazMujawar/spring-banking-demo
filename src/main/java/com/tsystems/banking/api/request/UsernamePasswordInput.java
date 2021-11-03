package com.tsystems.banking.api.request;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UsernamePasswordInput {
  @NotNull(message = "Username is required")
  private String username;

  @NotNull(message = "Password is required")
  @Length(min = 5, message = "Password must be at least 5 characters long")
  private String password;

  public UsernamePasswordInput() {}

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }
}

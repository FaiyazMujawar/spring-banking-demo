package com.tsystems.banking.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@ApiModel(description = "Login Request")
public class LoginRequest {
  @NotNull(message = "Username is required")
  @ApiModelProperty(
    name = "username",
    notes = "Username of the user",
    required = true
  )
  private String username;

  @NotNull(message = "Password is required")
  @Length(min = 5, message = "Password must be at least 5 characters long")
  @ApiModelProperty(
    name = "password",
    notes = "Password of the user",
    required = true
  )
  private String password;

  public LoginRequest() {}

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

package com.tsystems.banking.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

@ApiModel
public class RegisterRequest {
  @NotNull(message = "First name is required")
  @ApiModelProperty(
    name = "firstName",
    notes = "First name of the user",
    required = true
  )
  private String firstName;

  @NotNull(message = "Last name is required")
  @ApiModelProperty(
    name = "lastName",
    notes = "Last name of the user",
    required = true
  )
  private String lastName;

  @NotNull(message = "email is required")
  @Email(message = "Please provide a valid email")
  @ApiModelProperty(
    name = "email",
    notes = "Email of the user",
    required = true
  )
  private String email;

  @NotNull(message = "Password is required")
  @Min(value = 5, message = "Password must be at least 5 characters long")
  @ApiModelProperty(
    name = "password",
    notes = "Password of the user",
    required = true
  )
  private String password;

  @NotNull(message = "Contact is required")
  @Range(
    min = 1000000000,
    max = 9999999999L,
    message = "Contact must be 10 digits long"
  )
  @ApiModelProperty(
    name = "contact",
    notes = "Contact number of the user",
    required = true
  )
  private Long contact;

  public RegisterRequest() {}

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail(String email) {
    this.email = email;
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

  /**
   * @return the contact
   */
  public Long getContact() {
    return contact;
  }

  /**
   * @param contact the contact to set
   */
  public void setContact(Long contact) {
    this.contact = contact;
  }
}

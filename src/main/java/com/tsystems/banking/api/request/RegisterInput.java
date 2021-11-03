package com.tsystems.banking.api.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public class RegisterInput {
  @NotNull(message = "First name is required")
  private String firstName;

  @NotNull(message = "Last name is required")
  private String lastName;

  @NotNull(message = "email is required")
  @Email(message = "Please provide a valid email")
  private String email;

  @NotNull(message = "Password is required")
  @Min(value = 5, message = "Password must be at least 5 characters long")
  private String password;

  @NotNull(message = "Contact is required")
  @Range(
    min = 1000000000,
    max = 9999999999L,
    message = "Contact must be 10 digits long"
  )
  private Long contact;

  public RegisterInput() {}

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

package com.tsystems.banking.models;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Long contact;

  @OneToMany(mappedBy = "accountOwner")
  private List<Account> accounts;

  public User() {}

  /**
   * @return the accounts
   */
  public List<Account> getAccounts() {
    return accounts;
  }

  /**
   * @param accounts the accounts to set
   */
  public void setAccounts(List<Account> accounts) {
    this.accounts = accounts;
  }

  /**
   * @param id
   * @param firstName
   * @param lastName
   * @param username
   * @param email
   * @param password
   * @param contact
   */
  public User(
    Long id,
    String firstName,
    String lastName,
    String username,
    String email,
    String password,
    Long contact
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.contact = contact;
  }

  /**
   * @param firstName
   * @param lastName
   * @param username
   * @param email
   * @param password
   * @param contact
   */
  public User(
    String firstName,
    String lastName,
    String username,
    String email,
    String password,
    Long contact
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.contact = contact;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(Long id) {
    this.id = id;
  }

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

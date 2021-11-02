package com.tsystems.banking.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private Double balance;

  public Account() {}

  /**
   * @param id
   * @param userId
   * @param balance
   */
  public Account(Long id, Long userId, Double balance) {
    this.id = id;
    this.userId = userId;
    this.balance = balance;
  }

  public Account(Long userId, Double balance) {
    this.userId = userId;
    this.balance = balance;
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
   * @return the userId
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * @param userId the userId to set
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * @return the balance
   */
  public Double getBalance() {
    return balance;
  }

  /**
   * @param balance the balance to set
   */
  public void setBalance(Double balance) {
    this.balance = balance;
  }
}

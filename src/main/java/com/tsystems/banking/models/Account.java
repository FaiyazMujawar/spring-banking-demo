package com.tsystems.banking.models;

import static javax.persistence.FetchType.EAGER;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = EAGER)
  private User accountOwner;

  /**
   * @param accountOwner
   * @param balance
   */
  public Account(User accountOwner, Double balance) {
    this.setAccountOwner(accountOwner);
    this.balance = balance;
  }

  /**
   * @return the accountOwner
   */
  public User getAccountOwner() {
    return accountOwner;
  }

  /**
   * @param accountOwner the accountOwner to set
   */
  public void setAccountOwner(User accountOwner) {
    this.accountOwner = accountOwner;
  }

  @Column(nullable = false)
  private Double balance;

  public Account() {}

  /**
   * @param id
   * @param userId
   * @param balance
   */

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

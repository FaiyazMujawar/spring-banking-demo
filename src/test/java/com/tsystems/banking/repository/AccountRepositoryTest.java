package com.tsystems.banking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AccountRepositoryTest {
  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private UserRepository userRepository;

  private User user = new User(
    "John",
    "Doe",
    "johnDoe",
    "john@email.com",
    "123",
    123L
  );

  private Account account = new Account(user, 1000d);

  private Double minimumBalance = 2000d;

  @AfterEach
  public void tearDown() {
    accountRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Nested
  @DisplayName("Testing findByBalanceLessThan()")
  public class testFindByBalanceLessThan {

    @Test
    @DisplayName("Should return a list of length '1'")
    public void shouldReturnNonEmptyList() {
      userRepository.save(user);
      accountRepository.save(account);

      List<Account> accounts = accountRepository.findByBalanceLessThan(
        minimumBalance
      );

      assertThat(accounts.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should return an empty list")
    public void shouldReturnEmptyList() {
      List<Account> accounts = accountRepository.findByBalanceLessThan(
        minimumBalance
      );

      assertThat(accounts).isEmpty();
    }
  }
}

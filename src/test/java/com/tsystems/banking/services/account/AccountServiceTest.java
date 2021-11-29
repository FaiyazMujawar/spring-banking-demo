package com.tsystems.banking.services.account;

import com.tsystems.banking.exceptions.AccountNotFoundException;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.repository.AccountRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
  @Mock
  private AccountRepository accountRepository;

  private AccountService accountService;

  private static final Long accountId = 123L;
  private static final Double minimumBalance = 2000d;

  private User user = new User(
    "John",
    "Doe",
    "johnDoe",
    "john@email.com",
    "123",
    accountId
  );

  private Account account = new Account(user, 1000d);

  @BeforeEach
  void setUp() {
    accountService = new AccountServiceImplementation(accountRepository);
  }

  @Nested
  @DisplayName("Testing findById()")
  public class testFindById {

    @Test
    @DisplayName("Should return an account when account with ID exists")
    public void shouldReturnAccount() {
      Mockito
        .when(accountRepository.findById(accountId))
        .thenReturn(Optional.of(account));

      accountService.findById(accountId);

      Mockito.verify(accountRepository).findById(accountId);
    }

    @Test
    @DisplayName(
      "Should throw AccountNotFoundException when account does not exist"
    )
    public void shouldThrowNotFoundException() {
      Assertions
        .assertThatThrownBy(() -> accountService.findById(accountId))
        .isInstanceOf(AccountNotFoundException.class)
        .hasMessage(
          String.format(Constants.ACCOUNT_NOT_FOUND_ERROR, accountId)
        );
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when passed ID is null")
    public void shouldThrowIllegalArgumentExceptionOnNull() {
      Assertions
        .assertThatThrownBy(() -> accountService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_ID_ERROR);
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed ID is less than 1"
    )
    public void shouldThrowArgumentException() {
      Assertions
        .assertThatThrownBy(() -> accountService.findById(0L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_ID_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing createAccount")
  public class testCreateAccount {

    @Test
    @DisplayName("Should create account")
    public void shouldCreateAccount() {
      Mockito.when(accountRepository.save(Mockito.any())).thenReturn(account);

      accountService.createAccount(account);

      Mockito.verify(accountRepository).save(account);
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is null"
    )
    public void shouldThrowException() {
      Assertions
        .assertThatThrownBy(() -> accountService.createAccount(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_ACCOUNT_ARG_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing updateAccount()")
  public class testUpdateAccount {

    @Test
    @DisplayName("Should update the account details")
    public void shouldUpdateAccount() {
      Mockito
        .when(accountRepository.existsById(account.getId()))
        .thenReturn(true);

      accountService.updateAccount(account);

      Mockito.verify(accountRepository).save(account);
    }

    @Test
    @DisplayName(
      "Should throw AccountNotFoundException when account does not exist"
    )
    public void shouldThrowNotFoundException() {
      Mockito
        .when(accountRepository.existsById(account.getId()))
        .thenReturn(false);

      Assertions
        .assertThatThrownBy(() -> accountService.updateAccount(account))
        .isInstanceOf(AccountNotFoundException.class)
        .hasMessage(
          String.format(Constants.ACCOUNT_NOT_FOUND_ERROR, account.getId())
        );
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is null"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> accountService.updateAccount(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_ACCOUNT_ARG_ERROR);
    }
  }

  @Nested
  @DisplayName("Testing findAllWithMinimumBalance()")
  public class testFindAllWithMinimumBalance {

    @Test
    @DisplayName("Should return an empty list")
    public void shouldReturnEmptyList() {
      Mockito
        .when(accountRepository.findByBalanceLessThan(minimumBalance))
        .thenReturn(new ArrayList<Account>());

      Assertions
        .assertThat(accountService.findAllWithMinimumBalance(minimumBalance))
        .hasSize(0);
    }

    @Test
    @DisplayName("Should return a list of length '1'")
    public void shouldReturnList() {
      Mockito
        .when(accountRepository.findByBalanceLessThan(minimumBalance))
        .thenReturn(List.of(account));

      Assertions
        .assertThat(accountService.findAllWithMinimumBalance(minimumBalance))
        .hasSize(1);
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is null"
    )
    public void shouldThrowIllegalArgumentExceptionOnNull() {
      Assertions
        .assertThatThrownBy(
          () -> accountService.findAllWithMinimumBalance(null)
        )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_AMOUNT_ERROR);
    }

    @Test
    @DisplayName(
      "Should throw IllegalArgumentException when passed argument is less than '1'"
    )
    public void shouldThrowIllegalArgumentException() {
      Assertions
        .assertThatThrownBy(() -> accountService.findAllWithMinimumBalance(0d))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(Constants.INVALID_AMOUNT_ERROR);
    }
  }
}

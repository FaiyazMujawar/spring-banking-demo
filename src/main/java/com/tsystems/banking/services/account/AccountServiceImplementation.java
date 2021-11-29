package com.tsystems.banking.services.account;

import com.tsystems.banking.exceptions.AccountNotFoundException;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.repository.AccountRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImplementation implements AccountService {
  private final AccountRepository accountRepository;

  /**
   * @param accountRepository
   */
  @Autowired
  public AccountServiceImplementation(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Account createAccount(Account account) {
    if (account == null) {
      throw new IllegalArgumentException(Constants.INVALID_ACCOUNT_ARG_ERROR);
    }

    return accountRepository.save(account);
  }

  @Override
  public Account findById(Long accountId)
    throws AccountNotFoundException, IllegalArgumentException {
    if (accountId == null || accountId < 1) {
      throw new IllegalArgumentException(Constants.INVALID_ID_ERROR);
    }

    return accountRepository
      .findById(accountId)
      .orElseThrow(
        () ->
          new AccountNotFoundException(
            String.format(Constants.ACCOUNT_NOT_FOUND_ERROR, accountId)
          )
      );
  }

  @Override
  public Account updateAccount(Account account)
    throws AccountNotFoundException, IllegalArgumentException {
    if (account == null) {
      throw new IllegalArgumentException(Constants.INVALID_ACCOUNT_ARG_ERROR);
    }

    if (!accountRepository.existsById(account.getId())) {
      throw new AccountNotFoundException(
        String.format(Constants.ACCOUNT_NOT_FOUND_ERROR, account.getId())
      );
    }

    return accountRepository.save(account);
  }

  @Override
  public List<Account> findAllWithMinimumBalance(Double minimumBalance)
    throws IllegalArgumentException {
    if (minimumBalance == null || minimumBalance < 1) {
      throw new IllegalArgumentException(Constants.INVALID_AMOUNT_ERROR);
    }

    return accountRepository.findByBalanceLessThan(minimumBalance);
  }
}

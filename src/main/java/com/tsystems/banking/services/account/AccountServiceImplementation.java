package com.tsystems.banking.services.account;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.tsystems.banking.exceptions.AccountNotFoundException;
import com.tsystems.banking.exceptions.ApiException;
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
    return accountRepository.save(account);
  }

  @Override
  public Account findById(Long accountId) throws AccountNotFoundException {
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
  public Account updateAccount(Account account) {
    if (existsById(account.getId())) {
      return accountRepository.save(account);
    }

    throw new ApiException(
      NOT_FOUND,
      String.format(Constants.ACCOUNT_NOT_FOUND_ERROR, account.getId())
    );
  }

  @Override
  public Boolean existsById(Long accountId) {
    return accountRepository.existsById(accountId);
  }

  @Override
  public List<Account> findAllWithMinimumBalance(Double minimumBalance) {
    return accountRepository.findByBalanceLessThan(minimumBalance);
  }
}

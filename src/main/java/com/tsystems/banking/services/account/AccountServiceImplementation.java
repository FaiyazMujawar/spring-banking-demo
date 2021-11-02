package com.tsystems.banking.services.account;

import com.tsystems.banking.models.Account;
import com.tsystems.banking.repository.AccountRepository;
import java.util.Optional;
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
  public Optional<Account> findById(Long accountId) {
    return accountRepository.findById(accountId);
  }

  @Override
  public Account updateAccount(Account account) {
    return accountRepository.save(account);
  }
}

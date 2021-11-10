package com.tsystems.banking.services.account;

import com.tsystems.banking.exceptions.AccountNotFoundException;
import com.tsystems.banking.models.Account;
import java.util.List;

public interface AccountService {
  Account createAccount(Account account);

  Account findById(Long accountId) throws AccountNotFoundException;

  List<Account> findAllWithMinimumBalance(Double minimumBalance);

  Account updateAccount(Account account);

  Boolean existsById(Long accountId);
}

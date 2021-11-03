package com.tsystems.banking.services.account;

import com.tsystems.banking.models.Account;

public interface AccountService {
  Account createAccount(Account account);

  Account findById(Long accountId);

  Account updateAccount(Account account);

  Boolean existsById(Long accountId);
}

package com.tsystems.banking.services.account;

import com.tsystems.banking.models.Account;
import java.util.Optional;

public interface AccountService {
  Account createAccount(Account account);

  Optional<Account> findById(Long accountId);

  Account updateAccount(Account account);
}

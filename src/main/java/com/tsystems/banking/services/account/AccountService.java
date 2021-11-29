package com.tsystems.banking.services.account;

import com.tsystems.banking.exceptions.AccountNotFoundException;
import com.tsystems.banking.models.Account;
import java.util.List;

public interface AccountService {
  Account createAccount(Account account) throws IllegalArgumentException;

  Account findById(Long accountId)
    throws AccountNotFoundException, IllegalArgumentException;

  List<Account> findAllWithMinimumBalance(Double minimumBalance)
    throws IllegalArgumentException;

  Account updateAccount(Account account)
    throws AccountNotFoundException, IllegalArgumentException;
}

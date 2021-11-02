package com.tsystems.banking.controllers;

import static com.tsystems.banking.misc.Utils.getAuthenticatedUser;

import com.tsystems.banking.api.request.GetBalanceInput;
import com.tsystems.banking.api.request.UpdateBalanceInput;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.user.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/accounts")
public class AccountController {
  private final AccountService accountService;
  private final UserService userService;

  /**
   * @param accountService
   */
  public AccountController(
    AccountService accountService,
    UserService userService
  ) {
    this.accountService = accountService;
    this.userService = userService;
  }

  @GetMapping(path = "/balance")
  public ResponseEntity<?> getAccountBalance(
    @RequestBody GetBalanceInput getBalanceInput
  )
    throws Exception {
    Long accountId = getBalanceInput.getAccountId();
    Account account = accountService
      .findById(accountId)
      .orElseThrow(
        () ->
          new Exception(
            String.format("Account with account number %d not found", accountId)
          )
      );

    String username = (String) getAuthenticatedUser().getPrincipal();

    User user = userService
      .findByUsername(username)
      .orElseThrow(() -> new Exception("User not found"));

    if (!user.getId().equals(account.getUserId())) {
      throw new Exception("Unauthorized");
    }

    Map<String, Double> balance = new HashMap<>();
    balance.put("balance", account.getBalance());

    return ResponseEntity.ok().body(balance);
  }

  @PostMapping(path = "/deposit")
  public ResponseEntity<?> depositAmount(
    @RequestBody UpdateBalanceInput depositAmountInput
  )
    throws Exception {
    Long accountId = depositAmountInput.getAccountId();
    Account account = accountService
      .findById(accountId)
      .orElseThrow(
        () ->
          new Exception(
            String.format("Account with account number %d not found", accountId)
          )
      );

    String username = (String) getAuthenticatedUser().getPrincipal();

    User user = userService
      .findByUsername(username)
      .orElseThrow(() -> new Exception("User not found"));

    if (!user.getId().equals(account.getUserId())) {
      throw new Exception("Unauthorized");
    }

    account.setBalance(account.getBalance() + depositAmountInput.getAmount());

    accountService.updateAccount(account);

    Map<String, String> message = new HashMap<>();
    message.put("message", "Amount deposited");

    return ResponseEntity.ok().body(message);
  }

  @PostMapping(path = "/withdraw")
  public ResponseEntity<?> withdrawAmount(
    @RequestBody UpdateBalanceInput withdrawAmountInput
  )
    throws Exception {
    Long accountId = withdrawAmountInput.getAccountId();
    Double amount = withdrawAmountInput.getAmount();

    if (amount <= 1) {
      throw new Exception("Amount should be greater than 0");
    }

    Account account = accountService
      .findById(accountId)
      .orElseThrow(
        () ->
          new Exception(
            String.format("Account with account number %s not found", accountId)
          )
      );

    if (account.getBalance() < amount) {
      throw new Exception("Insufficient balance");
    }

    String username = (String) getAuthenticatedUser().getPrincipal();
    User user = userService
      .findByUsername(username)
      .orElseThrow(() -> new Exception("User not found"));

    if (!user.getId().equals(account.getUserId())) {
      throw new Exception("Unauthorized");
    }

    account.setBalance(account.getBalance() - amount);
    accountService.updateAccount(account);

    Map<String, String> message = new HashMap<>();
    message.put("message", "Amount withdrawn");

    return ResponseEntity.ok().body(message);
  }
}

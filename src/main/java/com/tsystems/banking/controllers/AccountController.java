package com.tsystems.banking.controllers;

import static com.tsystems.banking.misc.Utils.getAuthenticatedUser;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.api.request.GetBalanceInput;
import com.tsystems.banking.api.request.UpdateBalanceInput;
import com.tsystems.banking.api.response.SuccessfulResponse;
import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.user.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
  public ResponseEntity<SuccessfulResponse> getAccountBalance(
    @RequestBody @Valid GetBalanceInput getBalanceInput,
    HttpServletResponse response
  )
    throws ApiException {
    Long accountId = getBalanceInput.getAccountId();
    Account account = accountService.findById(accountId);

    String username = (String) getAuthenticatedUser().getPrincipal();

    User user = null;
    try {
      user = userService.findByUsername(username);
    } catch (ApiException e) {
      throw new ApiException(UNAUTHORIZED, "User not found");
    }

    if (!user.getId().equals(account.getUserId())) {
      throw new ApiException(
        UNAUTHORIZED,
        "Users can access only their own accounts"
      );
    }

    Map<String, Double> balance = new HashMap<>();
    balance.put("balance", account.getBalance());

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity.ok().body(new SuccessfulResponse(balance));
  }

  @PostMapping(path = "/deposit")
  public ResponseEntity<SuccessfulResponse> depositAmount(
    @RequestBody @Valid UpdateBalanceInput depositAmountInput,
    HttpServletResponse response
  )
    throws Exception {
    Long accountId = depositAmountInput.getAccountId();
    Account account = accountService.findById(accountId);

    String username = (String) getAuthenticatedUser().getPrincipal();

    User user = null;
    try {
      user = userService.findByUsername(username);
    } catch (ApiException e) {
      throw new ApiException(UNAUTHORIZED, "User not found");
    }

    if (!user.getId().equals(account.getUserId())) {
      throw new ApiException(
        UNAUTHORIZED,
        "Users can access only their own accounts"
      );
    }

    if (depositAmountInput.getAmount() <= 0) {
      throw new ApiException(BAD_REQUEST, "Amount must be greater than 0");
    }

    account.setBalance(account.getBalance() + depositAmountInput.getAmount());

    accountService.updateAccount(account);

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        new SuccessfulResponse(
          String.format("Amount {%d} deposited", depositAmountInput.getAmount())
        )
      );
  }

  @PostMapping(path = "/withdraw")
  public ResponseEntity<SuccessfulResponse> withdrawAmount(
    @RequestBody @Valid UpdateBalanceInput withdrawAmountInput,
    HttpServletResponse response
  )
    throws Exception {
    Long accountId = withdrawAmountInput.getAccountId();
    Double amount = withdrawAmountInput.getAmount();

    if (amount <= 0) {
      throw new ApiException(BAD_REQUEST, "Amount must be greater than 0");
    }

    Account account = accountService.findById(accountId);

    String username = (String) getAuthenticatedUser().getPrincipal();
    User user = null;
    try {
      user = userService.findByUsername(username);
    } catch (ApiException e) {
      throw new ApiException(UNAUTHORIZED, "User not found");
    }

    if (!user.getId().equals(account.getUserId())) {
      throw new ApiException(
        UNAUTHORIZED,
        "Users can access only their own accounts"
      );
    }

    if (account.getBalance() < amount) {
      throw new ApiException(BAD_REQUEST, "Insufficient balance");
    }

    account.setBalance(account.getBalance() - amount);
    accountService.updateAccount(account);

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        new SuccessfulResponse(String.format("Amount {%d} withdrawn", amount))
      );
  }
}

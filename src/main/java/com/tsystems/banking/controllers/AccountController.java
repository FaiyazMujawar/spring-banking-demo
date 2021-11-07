package com.tsystems.banking.controllers;

import static com.tsystems.banking.misc.Utils.getAmountDepositMail;
import static com.tsystems.banking.misc.Utils.getAmountWithdrawMail;
import static com.tsystems.banking.misc.Utils.getAuthenticatedUser;
import static com.tsystems.banking.misc.Utils.getDoubleWithPrecision;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.api.request.GetBalanceInput;
import com.tsystems.banking.api.request.UpdateBalanceInput;
import com.tsystems.banking.api.response.SuccessfulResponse;
import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.mail.MailService;
import com.tsystems.banking.services.user.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
  private final MailService mailService;

  /**
   * @param accountService
   * @param userService
   * @param mailService
   */
  @Autowired
  public AccountController(
    AccountService accountService,
    UserService userService,
    MailService mailService
  ) {
    this.accountService = accountService;
    this.userService = userService;
    this.mailService = mailService;
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
    } catch (Exception e) {
      throw new ApiException(UNAUTHORIZED, Constants.USER_NOT_FOUND_ERROR);
    }

    if (!user.getId().equals(account.getUserId())) {
      throw new ApiException(
        UNAUTHORIZED,
        Constants.UNAUTHORIZED_ACCOUNT_ACCESS_ERROR
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
      throw new ApiException(UNAUTHORIZED, Constants.USER_NOT_FOUND_ERROR);
    }

    if (!user.getId().equals(account.getUserId())) {
      throw new ApiException(
        UNAUTHORIZED,
        Constants.UNAUTHORIZED_ACCOUNT_ACCESS_ERROR
      );
    }

    if (depositAmountInput.getAmount() <= 0) {
      throw new ApiException(BAD_REQUEST, Constants.INVALID_AMOUNT_ERROR);
    }

    account.setBalance(account.getBalance() + depositAmountInput.getAmount());

    accountService.updateAccount(account);

    String mailBody = getAmountDepositMail(
      user.getFirstName(),
      accountId,
      depositAmountInput.getAmount()
    );

    mailService.sendHtmlMail(
      user.getEmail(),
      Constants.ACCOUNT_ACTIVITY_SUBJECT,
      mailBody
    );

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        new SuccessfulResponse(
          String.format(
            Constants.AMOUNT_DEPOSITED_MESSAGE,
            getDoubleWithPrecision(depositAmountInput.getAmount(), 2)
          )
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
      throw new ApiException(BAD_REQUEST, Constants.INVALID_AMOUNT_ERROR);
    }

    Account account = accountService.findById(accountId);

    String username = (String) getAuthenticatedUser().getPrincipal();
    User user = null;
    try {
      user = userService.findByUsername(username);
    } catch (ApiException e) {
      throw new ApiException(UNAUTHORIZED, Constants.USER_NOT_FOUND_ERROR);
    }

    if (!user.getId().equals(account.getUserId())) {
      throw new ApiException(
        UNAUTHORIZED,
        Constants.UNAUTHORIZED_ACCOUNT_ACCESS_ERROR
      );
    }

    if (account.getBalance() < amount) {
      throw new ApiException(BAD_REQUEST, Constants.INSUFFICIENT_BALANCE_ERROR);
    }

    account.setBalance(account.getBalance() - amount);
    accountService.updateAccount(account);

    String mailBody = getAmountWithdrawMail(
      user.getFirstName(),
      accountId,
      amount
    );

    mailService.sendHtmlMail(
      user.getEmail(),
      Constants.ACCOUNT_ACTIVITY_SUBJECT,
      mailBody
    );

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        new SuccessfulResponse(
          String.format(
            Constants.AMOUNT_WITHDRAWN_MESSAGE,
            getDoubleWithPrecision(amount, 2)
          )
        )
      );
  }
}

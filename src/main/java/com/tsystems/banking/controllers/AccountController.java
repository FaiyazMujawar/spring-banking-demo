package com.tsystems.banking.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.tsystems.banking.dto.DtoMapper;
import com.tsystems.banking.dto.request.GetBalanceRequest;
import com.tsystems.banking.dto.request.UpdateBalanceRequest;
import com.tsystems.banking.dto.response.GetBalanceResponse;
import com.tsystems.banking.dto.response.UpdateBalanceResponse;
import com.tsystems.banking.exceptions.AccountNotFoundException;
import com.tsystems.banking.exceptions.ApiException;
import com.tsystems.banking.exceptions.UserNotFoundException;
import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.misc.Utils;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.mail.MailService;
import com.tsystems.banking.services.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  @ApiOperation(
    value = "Get account balance",
    notes = "Controller for getting account balance"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Ok",
        response = GetBalanceResponse.class
      ),
    }
  )
  public ResponseEntity<GetBalanceResponse> getAccountBalance(
    @ApiParam(
      required = true,
      value = "Account number to get balance for"
    ) @RequestBody @Valid GetBalanceRequest getBalanceRequest,
    HttpServletResponse response
  )
    throws Exception {
    User user = null;
    Account account = null;

    Long accountId = getBalanceRequest.getAccountId();
    String username = (String) Utils.getAuthenticatedUser().getPrincipal();

    try {
      account = accountService.findById(accountId);
      user = userService.findByUsername(username);
    } catch (AccountNotFoundException e) {
      throw new ApiException(BAD_REQUEST, e.getLocalizedMessage());
    } catch (UserNotFoundException e) {
      throw new ApiException(UNAUTHORIZED, e.getLocalizedMessage());
    }

    if (!isAccountOwner(account, user)) {
      throw new ApiException(
        UNAUTHORIZED,
        Constants.UNAUTHORIZED_ACCOUNT_ACCESS_ERROR
      );
    }

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(DtoMapper.toGetBalanceResponse(account.getBalance()));
  }

  @PutMapping(path = "/deposit")
  @ApiOperation(
    value = "Deposit amount in account",
    notes = "Controller for depositing amount in account"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Ok",
        response = UpdateBalanceResponse.class
      ),
    }
  )
  public ResponseEntity<UpdateBalanceResponse> depositAmount(
    @ApiParam(
      required = true,
      value = "Account number and amount to deposit"
    ) @RequestBody @Valid UpdateBalanceRequest depositAmountRequest,
    HttpServletResponse response
  )
    throws Exception {
    User user = null;
    Account account = null;

    Long accountId = depositAmountRequest.getAccountId();
    String username = (String) Utils.getAuthenticatedUser().getPrincipal();

    try {
      account = accountService.findById(accountId);
      user = userService.findByUsername(username);
    } catch (AccountNotFoundException e) {
      throw new ApiException(BAD_REQUEST, e.getLocalizedMessage());
    } catch (UserNotFoundException e) {
      throw new ApiException(UNAUTHORIZED, e.getLocalizedMessage());
    }

    if (!isAccountOwner(account, user)) {
      throw new ApiException(
        UNAUTHORIZED,
        Constants.UNAUTHORIZED_ACCOUNT_ACCESS_ERROR
      );
    }

    account.setBalance(account.getBalance() + depositAmountRequest.getAmount());

    accountService.updateAccount(account);

    String mailBody = Utils.getAmountDepositMail(
      user.getFirstName(),
      accountId,
      depositAmountRequest.getAmount()
    );

    try {
      mailService.sendHtmlMail(
        user.getEmail(),
        Constants.ACCOUNT_ACTIVITY_SUBJECT,
        mailBody
      );
    } catch (Exception e) {
      System.err.println(
        "Deposit mail not sent, Error: " + e.getLocalizedMessage()
      );
    }

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        DtoMapper.toUpdateBalanceDto(
          String.format(
            Constants.AMOUNT_DEPOSITED_MESSAGE,
            Utils.getDoubleWithPrecision(depositAmountRequest.getAmount(), 2)
          )
        )
      );
  }

  @PutMapping(path = "/withdraw")
  @ApiOperation(
    value = "Withdraw amount from account",
    notes = "Controller for withdrawing amount from account"
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Ok",
        response = GetBalanceResponse.class
      ),
    }
  )
  public ResponseEntity<UpdateBalanceResponse> withdrawAmount(
    @ApiParam(
      required = true,
      value = "Account number and amount to withdraw"
    ) @RequestBody @Valid UpdateBalanceRequest withdrawAmountRequest,
    HttpServletResponse response
  )
    throws Exception {
    User user = null;
    Account account = null;

    Long accountId = withdrawAmountRequest.getAccountId();
    Double amount = withdrawAmountRequest.getAmount();
    String username = (String) Utils.getAuthenticatedUser().getPrincipal();

    try {
      account = accountService.findById(accountId);
      user = userService.findByUsername(username);
    } catch (AccountNotFoundException e) {
      throw new ApiException(BAD_REQUEST, e.getLocalizedMessage());
    } catch (UserNotFoundException e) {
      throw new ApiException(UNAUTHORIZED, e.getLocalizedMessage());
    }

    if (!isAccountOwner(account, user)) {
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

    String mailBody = Utils.getAmountWithdrawMail(
      user.getFirstName(),
      accountId,
      amount
    );

    try {
      mailService.sendHtmlMail(
        user.getEmail(),
        Constants.ACCOUNT_ACTIVITY_SUBJECT,
        mailBody
      );
    } catch (Exception e) {
      System.err.println(
        "Withdraw mail not sent, Error: " + e.getLocalizedMessage()
      );
    }

    response.setContentType(APPLICATION_JSON_VALUE);
    return ResponseEntity
      .ok()
      .body(
        DtoMapper.toUpdateBalanceDto(
          String.format(
            Constants.AMOUNT_WITHDRAWN_MESSAGE,
            Utils.getDoubleWithPrecision(amount, 2)
          )
        )
      );
  }

  private Boolean isAccountOwner(Account account, User user) {
    return account.getAccountOwner().getId().equals(user.getId());
  }
}

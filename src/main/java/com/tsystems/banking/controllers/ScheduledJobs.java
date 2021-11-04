package com.tsystems.banking.controllers;

import com.tsystems.banking.config.AppConfig;
import com.tsystems.banking.misc.Utils;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.mail.MailService;
import com.tsystems.banking.services.user.UserService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledJobs {
  private final AppConfig appConfig;
  private final AccountService accountService;
  private final UserService userService;
  private final MailService mailService;

  private final String MINIMUM_BALANCE_CHECK_CRON =
    "${scheduling.check_minimum_balance_cron}";

  /**
   * @param appConfig
   */
  public ScheduledJobs(
    AppConfig appConfig,
    AccountService accountService,
    UserService userService,
    MailService mailService
  ) {
    this.appConfig = appConfig;
    this.accountService = accountService;
    this.userService = userService;
    this.mailService = mailService;
  }

  @Scheduled(cron = MINIMUM_BALANCE_CHECK_CRON)
  public void checkMinimumBalance() {
    List<Account> minimumBalanceAccounts = accountService.findAllWithMinimumBalance(
      appConfig.getMinimumAccountBalance()
    );

    minimumBalanceAccounts
      .stream()
      .forEach(
        account -> {
          User user = userService.findById(account.getUserId());

          String mailBody = Utils.getLowBalanceAlertMail(
            user.getFirstName(),
            account.getId(),
            appConfig.getMinimumAccountBalance()
          );

          mailService.sendHtmlMail(
            user.getEmail(),
            "Low balance alert",
            mailBody
          );
        }
      );
  }
}

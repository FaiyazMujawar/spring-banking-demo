package com.tsystems.banking.config;

import static com.tsystems.banking.misc.Utils.getLowBalanceAlertMail;

import com.tsystems.banking.misc.Constants;
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
public class SchedulerConfiguration {
  private final AppConfig appConfig;
  private final AccountService accountService;
  private final UserService userService;
  private final MailService mailService;

  /**
   * @param appConfig
   */
  public SchedulerConfiguration(
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

  @Scheduled(cron = Constants.MINIMUM_BALANCE_CHECK_CRON)
  public void checkMinimumBalance() {
    List<Account> minimumBalanceAccounts = accountService.findAllWithMinimumBalance(
      appConfig.getMinimumAccountBalance()
    );

    minimumBalanceAccounts
      .stream()
      .forEach(
        account -> {
          User user = null;
          try {
            user = userService.findById(account.getUserId());
          } catch (Exception e) {
            System.err.println(
              Constants.USER_NOT_FOUND_ERROR + " " + account.getUserId()
            );
          }

          String mailBody = getLowBalanceAlertMail(
            user.getFirstName(),
            account.getId(),
            appConfig.getMinimumAccountBalance()
          );

          try {
            mailService.sendHtmlMail(
              user.getEmail(),
              Constants.ACCOUNT_ACTIVITY_SUBJECT,
              mailBody
            );
          } catch (Exception e) {
            System.err.println(
              "Alert mail not sent, Error: " + e.getLocalizedMessage()
            );
          }
        }
      );
  }
}

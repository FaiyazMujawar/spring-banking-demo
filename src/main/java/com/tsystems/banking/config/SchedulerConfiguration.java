package com.tsystems.banking.config;

import static com.tsystems.banking.misc.Utils.getLowBalanceAlertMail;

import com.tsystems.banking.misc.Constants;
import com.tsystems.banking.models.Account;
import com.tsystems.banking.models.User;
import com.tsystems.banking.services.account.AccountService;
import com.tsystems.banking.services.mail.MailService;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {
  private final AppConfig appConfig;
  private final AccountService accountService;
  private final MailService mailService;

  /**
   * @param appConfig
   */
  public SchedulerConfiguration(
    AppConfig appConfig,
    AccountService accountService,
    MailService mailService
  ) {
    this.appConfig = appConfig;
    this.accountService = accountService;
    this.mailService = mailService;
  }

  @Scheduled(cron = Constants.MINIMUM_BALANCE_CHECK_CRON)
  public void checkMinimumBalance() {
    Double minimumAccountBalance = appConfig.getMinimumAccountBalance();

    List<Account> minimumBalanceAccounts = accountService.findAllWithMinimumBalance(
      minimumAccountBalance
    );

    minimumBalanceAccounts
      .stream()
      .forEach(
        account -> {
          User accountOwner = account.getAccountOwner();

          String mailBody = getLowBalanceAlertMail(
            accountOwner.getFirstName(),
            account.getId(),
            minimumAccountBalance
          );

          try {
            mailService.sendHtmlMail(
              accountOwner.getEmail(),
              Constants.LOW_BALANCE_SUBJECT,
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

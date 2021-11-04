package com.tsystems.banking.misc;

import java.text.DecimalFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

  public static Authentication getAuthenticatedUser() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public static String getLowBalanceAlertMail(
    String name,
    Long accountNumber,
    Double minimumBalance
  ) {
    String body =
      "Dear %s," +
      "<br />" +
      "<br />" +
      "This email is to alert you that your account with account number <b>%d</b> has less balance than the minimum amount of <b>%s</b>. We urge you to please maintain the minimum balance" +
      "<br />" +
      "<br />" +
      "Thank you," +
      "<br />" +
      "Bank";

    return String.format(
      body,
      name,
      accountNumber,
      getDoubleWithPrecision(minimumBalance, 2)
    );
  }

  public static String getAmountDepositMail(
    String name,
    Long accountNumber,
    Double amount
  ) {
    String body =
      "Dear %s," +
      "<br />" +
      "<br />" +
      "This email is to inform you that your account with account number <b>%d</b> has been credited with amount <b>%s</b>." +
      "<br />" +
      "<br />" +
      "Thank you," +
      "<br />" +
      "Bank";

    return String.format(
      body,
      name,
      accountNumber,
      getDoubleWithPrecision(amount, 2)
    );
  }

  public static String getAmountWithdrawMail(
    String name,
    Long accountNumber,
    Double amount
  ) {
    String body =
      "Dear %s," +
      "<br />" +
      "<br />" +
      "This email is to inform you that your account with account number <b>%d</b> has been debited with amount <b>%s</b>." +
      "<br />" +
      "<br />" +
      "Thank you," +
      "<br />" +
      "Bank";

    return String.format(
      body,
      name,
      accountNumber,
      getDoubleWithPrecision(amount, 2)
    );
  }

  public static String getDoubleWithPrecision(Double number, int precision) {
    String pattern = "#.";
    for (int i = 0; i < precision; i++) {
      pattern += "#";
    }

    return new DecimalFormat(pattern).format(number);
  }
}

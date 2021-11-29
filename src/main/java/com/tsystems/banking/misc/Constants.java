package com.tsystems.banking.misc;

public interface Constants {
  String ACCOUNT_NOT_FOUND_ERROR = "Account with account number {%s} not found";

  String USER_NOT_FOUND_ERROR = "User not found";

  String UNAUTHORIZED_ACCOUNT_ACCESS_ERROR =
    "Users can access only their own accounts";

  String INVALID_AMOUNT_ERROR = "Amount must be greater than 0";

  String AMOUNT_DEPOSITED_MESSAGE = "Amount {%s} deposited";

  String AMOUNT_WITHDRAWN_MESSAGE = "Amount {%s} withdrawn";

  String ACCOUNT_ACTIVITY_SUBJECT = "Account activity update";

  String LOW_BALANCE_SUBJECT = "Low balance alert";

  String INSUFFICIENT_BALANCE_ERROR =
    "Insufficient balance to perform the operation";

  String EMAIL_IN_USE_ERROR = "Email already in use";

  String TOKEN_PREFIX = "Bearer ";

  String ACCESS_TOKEN_REQUIRED_ERROR = "Access token is required";

  String AUTH_HEADER_MALFORMED_ERROR = "Authorization header is malformed";

  String MINIMUM_BALANCE_CHECK_CRON =
    "${scheduling.check_minimum_balance_cron}";

  String INVALID_ID_ERROR = "Please provide valid ID";

  String INVALID_ACCOUNT_ARG_ERROR = "Please provide valid 'Account' object";

  String INVALID_USER_ARG_ERROR = "Please provide valid 'User' object";

  String INVALID_USERNAME_ERROR = "Please provide valid username";

  String INVALID_EMAIL_ERROR = "Please provide valid email";

  String INVALID_TOKEN_ERROR = "Please provide valid token";

  String INVALID_SUBJECT_ERROR = "Please provide valid token subject";

  String INVALID_ISSUER_ERROR = "Please provide valid token issuer";

  String INVALID_EXPIRATION_TIME_ERROR =
    "Please provide valid token expiration time";

  String JWT_AUTH_ERROR = "Invalid or corrupted access token";

  String REQUEST_BODY_UNREADABLE_ERROR = "Request body cannot be read properly";

  String INVALID_REQUEST_BODY_ERROR = "Please provide a valid body";
}

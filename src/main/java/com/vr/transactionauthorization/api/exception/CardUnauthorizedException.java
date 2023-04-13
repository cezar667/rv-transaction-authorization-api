package com.vr.transactionauthorization.api.exception;

public class CardUnauthorizedException extends RuntimeException {
  public CardUnauthorizedException(String message) {
    super(message);
  }

  public CardUnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }
}

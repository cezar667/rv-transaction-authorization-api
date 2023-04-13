package com.vr.transactionauthorization.api.exception;

public class CardUnauthorizedException extends RuntimeException {
  public CardUnauthorizedException(String message) {
    super(message);
  }
}

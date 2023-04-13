package com.vr.transactionauthorization.api.exception;

public class CardBadRequestException extends RuntimeException {
  public CardBadRequestException(String message) {
    super(message);
  }

  public CardBadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}

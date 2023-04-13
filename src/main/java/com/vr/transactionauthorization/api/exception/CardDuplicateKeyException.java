package com.vr.transactionauthorization.api.exception;

public class CardDuplicateKeyException extends RuntimeException {

  public CardDuplicateKeyException(String message) {
    super(message);
  }
}

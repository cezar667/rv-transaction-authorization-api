package com.vr.transactionauthorization.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CardDuplicateKeyException extends RuntimeException {

  public CardDuplicateKeyException(String message) {
    super(message);
  }

  public CardDuplicateKeyException(String message, Throwable cause) {
    super(message, cause);
  }
}

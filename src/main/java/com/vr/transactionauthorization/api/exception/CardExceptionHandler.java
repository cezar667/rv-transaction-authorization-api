package com.vr.transactionauthorization.api.exception;

import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CardExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(CardDuplicateKeyException.class)
  public ResponseEntity<Object> handleCardDuplicateKeyException(CardDuplicateKeyException ex) {
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(CardNotFoundException.class)
  public ResponseEntity<Object> handleCardNotFoundException(CardNotFoundException ex) {
    String errorMessage = "Cartão não encontrado";
    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
  }
}


package com.vr.transactionauthorization.api.controller;

import com.vr.transactionauthorization.api.dto.TransactionDto;
import com.vr.transactionauthorization.api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransactionController {

  private final TransactionService transactionService;

  @Autowired
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @ResponseStatus(code = HttpStatus.OK)
  @PostMapping
  public void efeturarTransacao(@RequestBody @Valid TransactionDto transactionDto) {
    transactionService.efetuarTransacao(transactionDto);
  }
}

package com.vr.transactionauthorization.api.controller;

import com.vr.transactionauthorization.api.dto.CardDto;
import com.vr.transactionauthorization.api.model.Card;
import com.vr.transactionauthorization.api.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
public class CardController {

  private final CardService cardService;

  @Autowired
  public CardController(CardService cardService){
    this.cardService = cardService;
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping
  public Card cadastrarCartao(@RequestBody @Valid CardDto cardDto){
    return cardService.createCard(cardDto);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping(value = "/{numeroCartao}")
  public Double buscarSaldo(@PathVariable String numeroCartao){
    return cardService.getCard(numeroCartao).getSaldo();
  }

}

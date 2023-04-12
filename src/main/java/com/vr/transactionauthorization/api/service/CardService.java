package com.vr.transactionauthorization.api.service;

import com.vr.transactionauthorization.api.dto.CardDto;
import com.vr.transactionauthorization.api.exception.CardDuplicateKeyException;
import com.vr.transactionauthorization.api.exception.CardNotFoundException;
import com.vr.transactionauthorization.api.model.Card;
import com.vr.transactionauthorization.api.repository.CardRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  public static final double SALDO_INICIAL = 500.0;
  private final CardRepository cardRepository;

  @Autowired
  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  public Card createCard(CardDto cardDto) {
    return Optional.of(
            cardRepository.insert(
                Card.builder()
                    .numeroCartao(cardDto.getNumeroCartao())
                    .senha(cardDto.getSenha())
                    .saldo(SALDO_INICIAL)
                    .build()))
        .orElseThrow(
            () ->
                new CardDuplicateKeyException(
                    "Erro ao tentar inserir. Este cartão ja existe: " + cardDto.toString()));
  }

  public Card getCard(String numeroCartao) {
    return cardRepository.findById(numeroCartao).orElseThrow(() -> new CardNotFoundException(""));
  }
}

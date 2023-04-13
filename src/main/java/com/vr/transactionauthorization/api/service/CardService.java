package com.vr.transactionauthorization.api.service;

import com.vr.transactionauthorization.api.dto.CardDto;
import com.vr.transactionauthorization.api.exception.CardDuplicateKeyException;
import com.vr.transactionauthorization.api.exception.CardNotFoundException;
import com.vr.transactionauthorization.api.model.Card;
import com.vr.transactionauthorization.api.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
    try {
      String senhaCriptografada = BCrypt.hashpw(cardDto.getSenha(), BCrypt.gensalt());
      return cardRepository.insert(Card.builder()
          .numeroCartao(cardDto.getNumeroCartao())
          .senha(senhaCriptografada)
          .saldo(SALDO_INICIAL)
          .build());
    } catch (DuplicateKeyException e) {
        throw new CardDuplicateKeyException("Erro ao tentar inserir. Este cartão ja existe: " + cardDto.toString());
    }
  }

  public Card getCard(String numeroCartao) {
    return cardRepository
        .findById(numeroCartao)
        .orElseThrow(() -> new CardNotFoundException("CARTAO_INEXISTENTE"));
  }

  public void updateCard(Card card){
    cardRepository.save(card);
  }

}

package com.vr.transactionauthorization.api.service;

import com.vr.transactionauthorization.api.dto.CardDto;
import com.vr.transactionauthorization.api.exception.CardDuplicateKeyException;
import com.vr.transactionauthorization.api.exception.CardNotFoundException;
import com.vr.transactionauthorization.api.model.Card;
import com.vr.transactionauthorization.api.repository.CardRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CardServiceTest {

  public static final String NUMERO_CARTAO = "1234567890123456";
  public static final String SENHA = "1234";

  private CardRepository cardRepository;

  private CardService cardService;

  private CardDto cardDto;
  private Card card;

  @BeforeEach
  public void setup(){
    cardRepository = Mockito.mock(CardRepository.class);
    cardService = new CardService(cardRepository);

    cardDto = new CardDto();
    cardDto.setNumeroCartao(NUMERO_CARTAO);
    cardDto.setSenha(SENHA);

    card = Card.builder()
        .numeroCartao(NUMERO_CARTAO)
        .senha(BCrypt.hashpw(SENHA, BCrypt.gensalt()))
        .saldo(CardService.SALDO_INICIAL)
        .build();
  }

  @Test
  void testCreateCard() {

    Mockito.when(cardRepository.insert(Mockito.any(Card.class))).thenReturn(card);

    Card result = cardService.createCard(cardDto);

    Assertions.assertEquals(card.getNumeroCartao(), result.getNumeroCartao());
    Assertions.assertEquals(card.getSenha(), result.getSenha());
    Assertions.assertEquals(card.getSaldo(), result.getSaldo(), 0.0);

    Mockito.verify(cardRepository, Mockito.times(1)).insert(Mockito.any(Card.class));

  }

  @Test
  void testCreateCardDuplicateKeyException() {

    Mockito.when(cardRepository.insert(Mockito.any(Card.class)))
        .thenThrow(DuplicateKeyException.class);

    try {
      cardService.createCard(cardDto);
      Assertions.fail("Deveria ter lançado uma exceção");
    } catch (CardDuplicateKeyException e) {
      // Sucesso
      Mockito.verify(cardRepository, Mockito.times(1)).insert(Mockito.any(Card.class));
    } catch (Exception e) {
      Assertions.fail("Lançou exceção incorreta: " + e.getClass().getName());
    }
  }

  @Test
  void testGetCard() {
    String numeroCartao = NUMERO_CARTAO;

    Mockito.when(cardRepository.findById(numeroCartao)).thenReturn(Optional.of(card));

    Card result = cardService.getCard(numeroCartao);

    Assertions.assertEquals(card.getNumeroCartao(), result.getNumeroCartao());
    Assertions.assertEquals(card.getSenha(), result.getSenha());
    Assertions.assertEquals(card.getSaldo(), result.getSaldo(), 0.0);

    Mockito.verify(cardRepository, Mockito.times(1)).findById(NUMERO_CARTAO);

  }

  @Test
  void testGetCardNotFoundException() {
    Mockito.when(cardRepository.findById(NUMERO_CARTAO))
        .thenReturn(Optional.empty());

    try {
      cardService.getCard(NUMERO_CARTAO);
      Assertions.fail("Deveria ter lançado uma exceção");
    } catch (CardNotFoundException e) {
      // Sucesso
      Mockito.verify(cardRepository, Mockito.times(1)).findById(NUMERO_CARTAO);
    } catch (Exception e) {
      Assertions.fail("Lançou exceção incorreta: " + e.getClass().getName());
    }
  }

  @Test
  void testUpdateCard() {

    cardService.updateCard(card);

    Mockito.verify(cardRepository, Mockito.times(1)).save(card);
  }
}

package com.vr.transactionauthorization.api.service;

import com.vr.transactionauthorization.api.dto.TransactionDto;
import com.vr.transactionauthorization.api.exception.CardUnauthorizedException;
import com.vr.transactionauthorization.api.model.Card;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

  public static final String NUMERO_CARTAO = "123456789";
  public static final String SENHA_123 = "senha123";

  private CardService cardService;

  @MockBean
  private RedisTemplate<String, Object> redisTemplate;

  private TransactionService transactionService;

  private TransactionDto transactionDto;
  private Card card;

  @BeforeEach
  void setUp() {
    transactionDto = new TransactionDto();
    transactionDto.setNumeroCartao(NUMERO_CARTAO);
    transactionDto.setSenha(SENHA_123);
    transactionDto.setValor(50.0);

    card = Card.builder()
        .numeroCartao(NUMERO_CARTAO)
        .senha(BCrypt.hashpw(SENHA_123, BCrypt.gensalt()))
        .saldo(100.0)
        .build();

    cardService = Mockito.mock(CardService.class);

    transactionService = new TransactionService(cardService, redisTemplate);

  }

  @Test
  @Description("Teste para verificar se o método efetuarTransacao trava o cartão quando ele já está em operação")
  void efetuarTransacao_lockCardWhenInOperation() {
    ValueOperations<String, Object> VALUE_OPERATIONS = Mockito.mock(
        ValueOperations.class);
    Mockito.when(redisTemplate.opsForValue()).thenReturn(VALUE_OPERATIONS);
    Mockito.when(VALUE_OPERATIONS.get(Mockito.any())).thenReturn(true).thenReturn(true).thenReturn(null);
    Mockito.when(VALUE_OPERATIONS.getAndDelete(Mockito.any())).thenReturn(true);

    Mockito.when(cardService.getCard(transactionDto.getNumeroCartao())).thenReturn(card);

    Assertions.assertDoesNotThrow(() -> transactionService.efetuarTransacao(transactionDto));

    Mockito.verify(cardService, Mockito.times(1)).getCard(NUMERO_CARTAO);
    Mockito.verify(VALUE_OPERATIONS, Mockito.times(3)).get(NUMERO_CARTAO);
    Mockito.verify(VALUE_OPERATIONS, Mockito.times(1)).set(Mockito.eq(card.getNumeroCartao()), Mockito.eq(true), Mockito.any(
        Duration.class));
    Mockito.verify(VALUE_OPERATIONS, Mockito.times(1)).getAndDelete(NUMERO_CARTAO);
  }

  @Test
  @Description("Teste para verificar se o método efetuarTransacao realiza uma transação bem-sucedida")
  void efetuarTransacao_successfulTransaction() {
    ValueOperations<String, Object> VALUE_OPERATIONS = Mockito.mock(
        ValueOperations.class);
    Mockito.when(redisTemplate.opsForValue()).thenReturn(VALUE_OPERATIONS);
    Mockito.when(VALUE_OPERATIONS.get(transactionDto.getNumeroCartao())).thenReturn(null);
    Mockito.when(cardService.getCard(transactionDto.getNumeroCartao())).thenReturn(card);

    transactionService.efetuarTransacao(transactionDto);

    Mockito.verify(VALUE_OPERATIONS, Mockito.times(1))
        .set(Mockito.eq(card.getNumeroCartao()), Mockito.eq(true), Mockito.any(
            Duration.class));
    Mockito.verify(VALUE_OPERATIONS, Mockito.times(1)).getAndDelete(card.getNumeroCartao());
    Mockito.verify(cardService, Mockito.times(1)).updateCard(card);
    Assertions.assertEquals(50.0, card.getSaldo(), 0.0);
  }

  @Test
  @Description("Teste para verificar se o método verifyCardForTransaction lança uma exceção quando a senha está incorreta")
  void verifyCardForTransaction_senhaInvalida() {
    String senhaIncorreta = "senhaErrada";
    Assertions.assertThrows(CardUnauthorizedException.class, () -> transactionService.verifyCardForTransaction(senhaIncorreta, transactionDto.getValor(), card));
  }

  @Test
  @Description("Teste para verificar se o método verifyCardForTransaction lança uma exceção quando o saldo é insuficiente")
  void verifyCardForTransaction_saldoInsuficiente() {
    Double valorMaiorQueSaldo = card.getSaldo() + 1;
    Assertions.assertThrows(CardUnauthorizedException.class, () -> transactionService.verifyCardForTransaction(transactionDto.getSenha(), valorMaiorQueSaldo, card));
  }

  @Test
  @Description("Verifica se a transação é válida, ou seja, se a senha está correta e o saldo é suficiente.")
  void verifyCardForTransaction_transactionValid() {
    Assertions.assertDoesNotThrow(() -> transactionService.verifyCardForTransaction(transactionDto.getSenha(), transactionDto.getValor(), card));
  }
}

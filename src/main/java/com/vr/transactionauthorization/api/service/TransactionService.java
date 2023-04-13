package com.vr.transactionauthorization.api.service;

import com.vr.transactionauthorization.api.dto.TransactionDto;
import com.vr.transactionauthorization.api.exception.CardNotFoundException;
import com.vr.transactionauthorization.api.exception.CardUnauthorizedException;
import com.vr.transactionauthorization.api.model.Card;
import java.time.Duration;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private final CardService cardService;

  private final RedisTemplate<String, Object> redisTemplate;

  @Autowired
  public TransactionService(CardService cardService, RedisTemplate<String, Object> redisTemplate) {
    this.cardService = cardService;
    this.redisTemplate = redisTemplate;
  }

  public void efetuarTransacao(TransactionDto transactionDto) {

    //Verifica se Cartão está em transação
    boolean inOperation = Optional.ofNullable(redisTemplate.opsForValue().get(transactionDto.getNumeroCartao())).isPresent();

    while (inOperation) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
        Thread.currentThread().interrupt();
      }
      inOperation = Optional.ofNullable(redisTemplate.opsForValue().get(transactionDto.getNumeroCartao())).isPresent();
    }

    Card card = cardService.getCard(transactionDto.getNumeroCartao());

    //Verifica se transação pode se autorizada
    verifyCardForTransaction(transactionDto.getSenha(), transactionDto.getValor(), card);

    //Insere trava no cartão
    redisTemplate.opsForValue().set(card.getNumeroCartao(), true, Duration.ofSeconds(10));

    card.debito(transactionDto.getValor());
    cardService.updateCard(card);

    //Remover trava do cartão
    redisTemplate.opsForValue().getAndDelete(card.getNumeroCartao());
  }

  public void verifyCardForTransaction(String senha, Double valor, Card card) {
    Optional.of(BCrypt.checkpw(senha, card.getSenha()))
        .filter(Boolean::booleanValue)
        .orElseThrow(() -> new CardUnauthorizedException("SENHA_INVALIDA"));

    Optional.of(card.getSaldo()-valor)
        .filter(saldo -> saldo >= 0)
        .orElseThrow(() -> new CardNotFoundException("SALDO_INSUFICIENTE"));

  }
}

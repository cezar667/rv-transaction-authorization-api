package com.vr.transactionauthorization.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(collection = "rv_card")
public class Card {

  @MongoId
  private String numeroCartao;

  private Double saldo;
  private String senha;

  public Double credito(Double valor) {
    this.saldo += valor;
    return saldo;
  }

  public Double debito(Double valor) {
    this.saldo -= valor;
    return saldo;
  }
}

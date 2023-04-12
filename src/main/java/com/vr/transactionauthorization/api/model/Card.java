package com.vr.transactionauthorization.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
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

}

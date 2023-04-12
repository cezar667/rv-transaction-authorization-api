package com.vr.transactionauthorization.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {
  private String numeroCartao;
  private String senha;
}

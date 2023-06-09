package com.vr.transactionauthorization.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {

  @NotNull
  private String numeroCartao;
  @NotNull
  private String senha;

  @Override
  public String toString() {
    return "{" +
        "\"numeroCartao\" = \"" + numeroCartao + "\"" +
        ", \"senha\" = \"" + senha + "\"" +
        "}";
  }
}

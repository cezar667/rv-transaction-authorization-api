package com.vr.transactionauthorization.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
  @NotNull
  private String numeroCartao;
  @NotNull
  private String senha;
  @NotNull
  private Double valor;

  @Override
  public String toString() {
    return "{" +
        "\"numeroCartao\" = \"" + numeroCartao + "\"" +
        ", \"senha\" = \"" + senha + "\"" +
        ", \"valor\" = \"" + valor + "\"" +
        "}";
  }
}

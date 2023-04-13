package com.vr.transactionauthorization.api.controller;

import com.vr.transactionauthorization.api.dto.TransactionDto;
import com.vr.transactionauthorization.api.service.CardService;
import com.vr.transactionauthorization.api.service.TransactionService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@SpringBootTest
public class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionService transactionService;

  @Autowired
  private TransactionController transactionController;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
  }

  @Test
  public void testEfetuarTransacao() throws Exception {
    String body = "{" +
        "\"numeroCartao\" : \"1234567890123456\"" +
        ", \"senha\" : \"123456\"" +
        ", \"valor\" : 100.0" +
        "}";

    mockMvc.perform(MockMvcRequestBuilders.post("/transacoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(MockMvcResultMatchers.status().isOk());

    Mockito.verify(transactionService, Mockito.times(1)).efetuarTransacao(Mockito.any(
        TransactionDto.class));
  }
}

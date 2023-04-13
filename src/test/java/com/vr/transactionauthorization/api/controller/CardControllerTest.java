package com.vr.transactionauthorization.api.controller;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr.transactionauthorization.api.dto.CardDto;
import com.vr.transactionauthorization.api.model.Card;
import com.vr.transactionauthorization.api.service.CardService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@SpringBootTest
class CardControllerTest {

  public static final String NUMERO_CARTAO = "1234123412341234";
  public static final String SENHA = "1234";
  public static final double SALDO = 100.;

  @MockBean
  private CardService cardService;

  @Autowired
  private CardController controller;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private Card card;
  private CardDto cardDto;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    card = Card.builder().numeroCartao(NUMERO_CARTAO).saldo(SALDO).senha(SENHA).build();
    cardDto = new CardDto();
    cardDto.setSenha(SENHA);
    cardDto.setNumeroCartao(NUMERO_CARTAO);

    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  void testCadastrarCartao() throws Exception {

    Mockito.when(cardService.createCard(Mockito.any(CardDto.class))).thenReturn(card);
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/cartoes")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(cardDto)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$.numeroCartao", Matchers.is(NUMERO_CARTAO)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.saldo", Matchers.is(SALDO)));


    Mockito.verify(cardService, Mockito.times(1))
        .createCard(Mockito.any(CardDto.class));
    }

  @Test
  void testCadastrarCartaoWithInvalidRequest() throws Exception {

    mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(""))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testBuscarSaldo() throws Exception {

    Mockito.when(cardService.getCard(NUMERO_CARTAO)).thenReturn(card);

    mockMvc.perform(MockMvcRequestBuilders.get("/cartoes/"+NUMERO_CARTAO)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(SALDO)));

    Mockito.verify(cardService, Mockito.times(1)).getCard(NUMERO_CARTAO);
  }

}

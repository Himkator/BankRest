package com.example.bankcards.controller;

import com.example.bankcards.service.CardControllerTestConfig;
import com.example.bankcards.dto.CardTransferRequest;
import com.example.bankcards.service.CardService;
import com.example.bankcards.dto.CardDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@Import(CardControllerTestConfig.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardService cardService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCard() throws Exception {
        CardDTO mockCard = new CardDTO(); // Заполните остальные поля

        Mockito.when(cardService.createCard()).thenReturn(mockCard);

        mockMvc.perform(post("/api/v1/cards/createCard")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"someField\": \"someValue\" }")) // Замените на валидный JSON
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardNumber").value("1111222233334444"));
    }

    @Test
    void testGetAllCards() throws Exception {
        Page<CardDTO> mockPage = new PageImpl<>(List.of(new CardDTO()));

        Mockito.when(cardService.getAllCards(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testTransferSuccess() throws Exception {
        CardTransferRequest transferDTO = new CardTransferRequest();
        transferDTO.setFromCardId(1L);
        transferDTO.setToCardId(2L);
        transferDTO.setAmount(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/cards/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }
}

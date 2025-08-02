package com.example.bankcards.factories;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardFactory {
    public CardDTO toDto(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .cardNumber(maskCardNumber(card.getCardNumber()))
                .balance(card.getBalance())
                .userId(card.getUser().getId())
                .expirationDate(card.getExpirationDate())
                .status(String.valueOf(card.getStatus()))
                .build();
    }

    private String maskCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(12);
    }
}

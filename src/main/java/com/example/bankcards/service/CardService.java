package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardTransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factories.CardFactory;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.specifications.CardSpecification;
import com.example.bankcards.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.bankcards.exception.IllegalStateException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardFactory cardFactory;

    public CardDTO createCard() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("Пользователь не найден");
        }
        Card card = Card.builder()
                .cardNumber(generateUniqueCardNumber())
                .user(currentUser)
                .expirationDate(LocalDate.now().plusYears(4))
                .build();

        card = cardRepository.saveAndFlush(card);

        return cardFactory.toDto(card);
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateRandomCardNumber();
        } while (cardRepository .existsByCardNumber(cardNumber));
        return cardNumber;
    }

    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    public Page<CardDTO> getAllCards(User user, String cardNumber, CardStatus status, BigDecimal minBalance, BigDecimal maxBalance, LocalDate expirationAfter, LocalDate expirationBefore, Pageable pageable) {
        Specification<Card> spec = CardSpecification.filterBy(
                user.getId(), cardNumber, status, minBalance, maxBalance, expirationAfter, expirationBefore
        );

        return cardRepository.findAll(spec, pageable)
                .map(cardFactory::toDto);
    }

    public CardDTO getCardById(int id, User user) {
        return cardFactory.toDto(cardRepository.findByIdAndUser((long) id, user));
    }
    @Transactional
    public void transfer(CardTransferRequest request, User user) {
        Card from = cardRepository.findByIdAndUser(request.getFromCardId(), user);
        Card to = cardRepository.findByIdAndUser(request.getToCardId(), user);
        if(from == null || to == null)
            throw new NotFoundException("Card not found");

        if (from.getStatus() != CardStatus.ACTIVE || to.getStatus() != CardStatus.ACTIVE)
            throw new IllegalStateException("Both cards must be active");

        if (from.getBalance().compareTo(request.getAmount()) < 0)
            throw new IllegalStateException("Insufficient funds");

        from.setBalance(from.getBalance().subtract(request.getAmount()));
        to.setBalance(to.getBalance().add(request.getAmount()));

        cardRepository.saveAll(List.of(from, to));
    }

    public void blockCard(int id, User user) {
        Card card = cardRepository.findByIdAndUser((long) id, user);
        if(card == null)
            throw new NotFoundException("Card not found");

        if(card.getStatus() == CardStatus.BLOCKED)
            throw new IllegalStateException("Card is already blocked");

        card.setStatus(CardStatus.PENDING_BLOCK);
        cardRepository.save(card);
    }
}

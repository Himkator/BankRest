package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.factories.CardFactory;
import com.example.bankcards.factories.UserFactory;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.specifications.CardSpecification;
import com.example.bankcards.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.bankcards.exception.IllegalStateException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardFactory cardFactory;
    private final UserFactory userFactory;

    public Page<CardDTO> getAllCards(String cardNumber,
                                     CardStatus status,
                                     BigDecimal minBalance,
                                     BigDecimal maxBalance,
                                     LocalDate expirationAfter,
                                     LocalDate expirationBefore,
                                     Pageable pageable) {
        Specification<Card> spec = CardSpecification.filterBy(
                null, cardNumber, status, minBalance, maxBalance, expirationAfter, expirationBefore
        );

        return cardRepository.findAll(spec, pageable)
                .map(cardFactory::toDto);
    }

    public Page<UserDTO> getAllUsers(String email,
                                     String phone,
                                     String name,
                                     String surname,
                                     Boolean isActive,
                                     Pageable pageable) {

        Specification<User> spec = UserSpecification.filterBy(email, phone, name, surname, isActive);
        return userRepository.findAll(spec, pageable)
                .map(userFactory::toDto);
    }

    public void blockCard(long id) {
        Card card = cardRepository.findById(id).orElseThrow(()->new NotFoundException("Card not found"));

        if(card.getStatus() == CardStatus.BLOCKED)
            throw new IllegalStateException("Card is already blocked");

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    public void activateCard(long id) {
        Card card = cardRepository.findById(id).orElseThrow(()->new NotFoundException("Card not found"));

        if(card.getStatus() == CardStatus.ACTIVE)
            throw new IllegalStateException("Card is already active");

        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }

    public void activateUser(long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));

        if(user.getIsActive())
            throw new IllegalStateException("User is already active");

        user.setIsActive(true);
        userRepository.save(user);
    }

    public void blockUser(long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NotFoundException("User not found"));

        if(!user.getIsActive())
            throw new IllegalStateException("User is already blocked");

        user.setIsActive(false);
        userRepository.save(user);
    }
}

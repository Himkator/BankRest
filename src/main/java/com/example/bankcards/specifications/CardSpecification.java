package com.example.bankcards.specifications;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CardSpecification {
    public static Specification<Card> filterBy(
            Long userId,
            String cardNumber,
            CardStatus status,
            BigDecimal minBalance,
            BigDecimal maxBalance,
            LocalDate expirationAfter,
            LocalDate expirationBefore
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (userId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("user").get("id"), userId));
            }

            if (cardNumber != null && !cardNumber.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("cardNumber")), "%" + cardNumber.toLowerCase() + "%"));
            }

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (minBalance != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("balance"), minBalance));
            }

            if (maxBalance != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("balance"), maxBalance));
            }

            if (expirationAfter != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("expirationDate"), expirationAfter));
            }

            if (expirationBefore != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("expirationDate"), expirationBefore));
            }

            return predicate;
        };
    }
}

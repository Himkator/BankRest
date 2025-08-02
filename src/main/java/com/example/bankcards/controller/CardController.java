package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardTransferRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
@Tag(name = "card_method", description = "Operations related to user cards")
public class CardController {
    private final CardService cardService;

    @Operation(summary = "Create a new card", description = "Generates and assigns a new card to the current user")
    @ApiResponse(responseCode = "201", description = "Card successfully created")
    @PostMapping("/createCard")
    public ResponseEntity<CardDTO> createCard() {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard());
    }

    @Operation(
            summary = "Get all cards",
            description = "Returns paginated list of user's cards filtered by optional parameters"
    )
    @Parameters({
            @Parameter(name = "cardNumber", description = "Filter by card number"),
            @Parameter(name = "status", description = "Filter by card status"),
            @Parameter(name = "minBalance", description = "Filter by minimum card balance"),
            @Parameter(name = "maxBalance", description = "Filter by maximum card balance"),
            @Parameter(name = "expirationAfter", description = "Filter by cards expiring after this date (yyyy-MM-dd)"),
            @Parameter(name = "expirationBefore", description = "Filter by cards expiring before this date (yyyy-MM-dd)"),
            @Parameter(name = "page", description = "Page number (zero-based index)", hidden = false),
            @Parameter(name = "size", description = "Page size", hidden = false),
            @Parameter(name = "sort", description = "Sort by fields (e.g. balance,expirationDate)", hidden = false)
    })
    @ApiResponse(responseCode = "200", description = "Cards fetched successfully")
    @GetMapping
    public ResponseEntity<Page<CardDTO>> getCards(
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) CardStatus status,
            @RequestParam(required = false) BigDecimal minBalance,
            @RequestParam(required = false) BigDecimal maxBalance,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationBefore,
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                cardService.getAllCards(user, cardNumber, status, minBalance, maxBalance, expirationAfter, expirationBefore, pageable)
        );
    }

    @Operation(summary = "Get card by ID", description = "Returns a card by its ID if it belongs to the user")
    @ApiResponse(responseCode = "200", description = "Card found")
    @ApiResponse(responseCode = "404", description = "Card not found")
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@Parameter(description = "Card ID") @PathVariable int id, @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cardService.getCardById(id, user));
    }

    @Operation(summary = "Transfer money", description = "Transfers money from one card to another")
    @ApiResponse(responseCode = "200", description = "Transfer successful")
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferCard(@Valid @RequestBody CardTransferRequest request, @Parameter(hidden = true) @AuthenticationPrincipal User user){
        cardService.transfer(request, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Block card", description = "Blocks a card by ID if it belongs to the user")
    @ApiResponse(responseCode = "200", description = "Card blocked successfully")
    @ApiResponse(responseCode = "404", description = "Card not found")
    @PostMapping("/{id}/block")
    public ResponseEntity<Void> blockCard(@Parameter(description = "Card ID") @PathVariable int id, @Parameter(hidden = true)  @AuthenticationPrincipal User user){
        cardService.blockCard(id, user);
        return ResponseEntity.ok().build();
    }
}

package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Admin Controller", description = "Operations available only for admins")
public class AdminController {
    private final AdminService service;

    @Operation(
            summary = "Get a list of cards",
            description = "Retrieve paginated cards filtered by card number, status, balance range, and expiration date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of cards successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/cards")
    public ResponseEntity<Page<CardDTO>> getCards(
            @Parameter(description = "Card number (optional)")
            @RequestParam(required = false) String cardNumber,
            @Parameter(description = "Card status (optional)")
            @RequestParam(required = false) CardStatus status,
            @Parameter(description = "Minimum balance (optional)")
            @RequestParam(required = false) BigDecimal minBalance,
            @Parameter(description = "Maximum balance (optional)")
            @RequestParam(required = false) BigDecimal maxBalance,
            @Parameter(description = "Expiration date after (optional)", example = "2025-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationAfter,
            @Parameter(description = "Expiration date before (optional)", example = "2030-01-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationBefore,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(
                service.getAllCards(cardNumber, status, minBalance, maxBalance, expirationAfter, expirationBefore, pageable)
        );
    }

    @Operation(
            summary = "Get a list of users",
            description = "Retrieve paginated users filtered by email, phone, name, surname, and activation status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users successfully retrieved"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getUsers(
            @Parameter(description = "User email (optional)")
            @RequestParam(required = false) String email,
            @Parameter(description = "Phone number (optional)")
            @RequestParam(required = false) String phone,
            @Parameter(description = "User's first name (optional)")
            @RequestParam(required = false) String name,
            @Parameter(description = "User's surname (optional)")
            @RequestParam(required = false) String surname,
            @Parameter(description = "Activation status (optional)")
            @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 10) Pageable pageable
    ){
        return ResponseEntity.ok(
                service.getAllUsers(email, phone, name, surname, isActive, pageable)
        );
    }

    @Operation(
            summary = "Block a card",
            description = "Block a specific card by its ID. Only accessible by admins."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card successfully blocked"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/cards/{id}/block")
    public ResponseEntity<Void> blockCard(
            @Parameter(description = "Card ID to block", required = true)
            @PathVariable long id
    ) {
        service.blockCard(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Activate a card",
            description = "Activate a specific card by its ID. Only accessible by admins."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card successfully activated"),
            @ApiResponse(responseCode = "404", description = "Card not found")
    })
    @PostMapping("/cards/{id}/activate")
    public ResponseEntity<Void> activateCard(
            @Parameter(description = "Card ID to activate", required = true)
            @PathVariable long id
    ) {
        service.activateCard(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Activate a user",
            description = "Activate a specific user by its ID. Only accessible by admins."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully activated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{id}/activate")
    public ResponseEntity<Void> activateUser(
            @Parameter(description = "User ID to activate", required = true)
            @PathVariable long id
    ){
        service.activateUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Activate a user",
            description = "Activate a specific user by its ID. Only accessible by admins."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully activated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{id}/block")
    public ResponseEntity<Void> blockUser(
            @Parameter(description = "User ID to block", required = true)
            @PathVariable long id
    ){
        service.blockUser(id);
        return ResponseEntity.ok().build();
    }
}

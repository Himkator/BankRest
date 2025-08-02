package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardTransferRequest {
    @NotNull(message = "fromCardId is mandatory")
    private Long fromCardId;
    @NotNull(message = "toCardId is mandatory")
    private Long toCardId;
    @Positive(message = "Amount need to be positive")
    private BigDecimal amount;
}

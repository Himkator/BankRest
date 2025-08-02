package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Authentication response containing JWT token and user info")
public class AuthResponse {
    @Schema(description = "JWT access token")
    private String token;
}

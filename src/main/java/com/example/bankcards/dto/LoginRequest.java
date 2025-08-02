package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Login json")
public class LoginRequest {
    @Schema(description = "email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Need to be email")
    private String email;
    @Schema(description = "password")
    @NotBlank(message = "Password is mandatory")
    private String password;
}

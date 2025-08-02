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
@Schema(description = "Register json")
public class RegisterRequest {
    @Schema(description = "email")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Need to be email")
    private String email;
    @Schema(description = "phone")
    @NotBlank(message = "Phone is mandatory")
    private String phone;
    @Schema(description = "name")
    @NotBlank(message = "Name is mandatory")
    private String name;
    @Schema(description = "surname")
    @NotBlank(message = "Surname is mandatory")
    private String surname;
    @Schema(description = "password")
    @NotBlank(message = "Password is mandatory")
    private String password;
}

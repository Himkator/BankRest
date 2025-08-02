package com.example.bankcards.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String phone;
    private String name;
    private String surname;
    private Boolean isActive;
    private String role;
}

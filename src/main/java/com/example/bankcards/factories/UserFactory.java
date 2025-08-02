package com.example.bankcards.factories;

import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {
    public UserDTO toDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .name(user.getName())
                .surname(user.getSurname())
                .isActive(user.getIsActive())
                .role(user.getRole().toString())
                .build();
    }
}

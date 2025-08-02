package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.util.JWTUtils;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestConfiguration
public class CardControllerTestConfig {

    @Bean
    public CardService cardService() {
        return Mockito.mock(CardService.class);
    }

    @Bean
    public JWTUtils jwtUtils() {
        JWTUtils mock = Mockito.mock(JWTUtils.class);
        when(mock.generateToken(any())).thenReturn("mock-token");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("mock@example.com");

        when(mock.extractUsername("mock-token")).thenReturn(String.valueOf(mockUser));
        return mock;
    }

    @Bean
    public UserDetailService userDetailService() {
        return Mockito.mock(UserDetailService.class);
    }
}
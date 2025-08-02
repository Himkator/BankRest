package com.example.bankcards.service;

import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.BadCredentialsException;
import com.example.bankcards.exception.IllegalStateException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null)
            throw new IllegalStateException("Username is already exists");

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .surname(request.getSurname())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = repository.saveAndFlush(user);

        if (savedUser == null || savedUser.getId() <= 0)
            throw new RuntimeException("User could not be saved");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtUtils.generateToken(savedUser));

        return authResponse;
    }

    public AuthResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail());

        if (user == null)
            throw new UsernameNotFoundException("User not found");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtUtils.generateToken(user));
        return authResponse;
    }
}

package com.example.bankcards.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class BadCredentialsException extends RuntimeException {
    private final String message;

    public BadCredentialsException(String message){
        super(message);
        this.message = message;
    }
}

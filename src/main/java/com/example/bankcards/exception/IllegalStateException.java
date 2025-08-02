package com.example.bankcards.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IllegalStateException extends RuntimeException{
    private final String message;

    public IllegalStateException(String message) {
        super(message);
        this.message = message;
    }
}

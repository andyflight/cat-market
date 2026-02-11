package com.example.catsmarket.domain.exceptions;

public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException(String message) {
        super(message);
    }
}

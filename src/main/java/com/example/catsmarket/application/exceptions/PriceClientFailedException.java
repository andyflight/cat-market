package com.example.catsmarket.application.exceptions;

public class PriceClientFailedException extends RuntimeException {
    public PriceClientFailedException(String message) {
        super(message);
    }
}

package com.example.catsmarket.application.exceptions;

public class PriceNotValidException extends RuntimeException {

    public static final String PriceNotValidMessageTemplate = "Price not valid: %s";

    public PriceNotValidException(String message) {
        super(String.format(PriceNotValidMessageTemplate, message));
    }
}

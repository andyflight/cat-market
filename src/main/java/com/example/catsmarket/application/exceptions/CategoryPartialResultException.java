package com.example.catsmarket.application.exceptions;

public class CategoryPartialResultException extends RuntimeException {

    public static final String CategoryPartialResultMessageTemplate = "The following categories were not found: %s";

    public CategoryPartialResultException(String message) {
        super(String.format(CategoryPartialResultMessageTemplate, message));
    }
}

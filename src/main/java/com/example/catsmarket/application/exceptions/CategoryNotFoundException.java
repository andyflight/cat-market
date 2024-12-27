package com.example.catsmarket.application.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public static final String CategoryNotFoundMessageTemplate = "Category with name %s not found";

    public CategoryNotFoundException(String message) {
        super(String.format(CategoryNotFoundMessageTemplate, message));
    }
}

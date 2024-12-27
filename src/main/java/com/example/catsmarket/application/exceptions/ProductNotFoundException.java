package com.example.catsmarket.application.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public static final String ProductNotFoundMessageTemplate = "Product with code %s not found";

    public ProductNotFoundException(String message) {
        super(String.format(ProductNotFoundMessageTemplate, message));
    }
}

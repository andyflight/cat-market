package com.example.catsmarket.application.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public static final String CustomerNotFoundMessageTemplate = "Customer with reference %s not found";

    public CustomerNotFoundException(String message) {
        super(String.format(CustomerNotFoundMessageTemplate, message));
    }
}

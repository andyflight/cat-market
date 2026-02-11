package com.example.catsmarket.application.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public static final String OrderNotFoundMessageTemplate = "Order with number %s not found";

    public OrderNotFoundException(String message) {
        super(String.format(OrderNotFoundMessageTemplate, message));
    }
}

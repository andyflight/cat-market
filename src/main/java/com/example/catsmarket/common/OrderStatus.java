package com.example.catsmarket.common;

public enum OrderStatus implements Descriptive {
    PENDING("Order placed, waiting for payment"),
    PROCESSING("Order confirmed and being prepared"),
    CANCELED("Order cancelled"),
    COMPLETED("Order successfully delivered to the customer"),
    RETURNED("Order returned by the customer");

    OrderStatus(String description) {
        this.description = description;
    }

    @Override
    public String getDescription(){
        return description ;
    }

    private final String description;
}

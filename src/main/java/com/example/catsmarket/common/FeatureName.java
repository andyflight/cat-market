package com.example.catsmarket.common;

public enum FeatureName implements Descriptive {

    DISCOUNT("discount"),
    PRICE_VALIDATION("price-validation");

    private final String description;

    FeatureName(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

}

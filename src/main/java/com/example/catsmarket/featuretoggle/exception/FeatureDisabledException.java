package com.example.catsmarket.featuretoggle.exception;

public class FeatureDisabledException extends RuntimeException {

    public static final String featureDisabledMessageTemplate = "Feature %s is disabled";

    public FeatureDisabledException(String message) {
        super(String.format(featureDisabledMessageTemplate, message));
    }
}

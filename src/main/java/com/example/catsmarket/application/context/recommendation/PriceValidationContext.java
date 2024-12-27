package com.example.catsmarket.application.context.recommendation;


import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PriceValidationContext {
    Boolean isValidated;
}

package com.example.catsmarket.application.context.price;


import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PriceValidationContext {
    Boolean isValidated;
}

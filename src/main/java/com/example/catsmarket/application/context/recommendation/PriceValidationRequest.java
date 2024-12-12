package com.example.catsmarket.application.context.recommendation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record PriceValidationRequest(
        @NotNull(message = "price cannot be null")
        @Positive(message = "price must be greater than zero")
        Double price
) {
}

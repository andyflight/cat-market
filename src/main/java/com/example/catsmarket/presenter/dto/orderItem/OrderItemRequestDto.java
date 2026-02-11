package com.example.catsmarket.presenter.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Schema(description = "Request containing details of an order item")
public record OrderItemRequestDto(
        @NotNull(message = "Product code cannot be null")
        @Pattern(regexp = "\\d{12,13}", message = "Product code supports only upc/ean format")
        @Schema(description = "UPC/EAN code of the product", example = "123456789012")
        String productCode,

        @NotNull(message = "Products quantity cannot be null")
        @Positive(message = "Products quantity must be greater than zero")
        @Schema(description = "Quantity of the product ordered", example = "2")
        Integer quantity,

        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than zero")
        @Schema(description = "Price of the product at the time of order", example = "15.99")
        Double currentPrice
) {
}
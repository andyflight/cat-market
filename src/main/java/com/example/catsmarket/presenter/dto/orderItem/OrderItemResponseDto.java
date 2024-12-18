package com.example.catsmarket.presenter.dto.orderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Schema(description = "Response containing details of an order item")
public record OrderItemResponseDto (
        @Schema(description = "Quantity of the product ordered", example = "2")
        Integer quantity,

        @Schema(description = "UPC/EAN code of the product", example = "123456789012")
        String productCode,

        @Schema(description = "Price of the product at the time of order", example = "15.99")
        Double productOldPrice
) {
}
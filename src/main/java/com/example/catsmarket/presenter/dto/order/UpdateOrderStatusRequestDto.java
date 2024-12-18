package com.example.catsmarket.presenter.dto.order;

import com.example.catsmarket.common.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Schema(description = "Request to update the status of an order")
public record  UpdateOrderStatusRequestDto(
        @NotNull(message = "Order status cannot be null")
        @Schema(description = "New status for the order", example = "PROCESSING")
        OrderStatus orderStatus
) {
}

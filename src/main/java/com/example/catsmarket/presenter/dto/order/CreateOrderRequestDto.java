package com.example.catsmarket.presenter.dto.order;

import com.example.catsmarket.presenter.dto.orderItem.OrderItemRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Builder
@Jacksonized
@Schema(description = "Request to create a new order")
public record CreateOrderRequestDto(
        @NotNull(message = "Customer reference cannot be null")
        @UUID(message = "Customer reference must be in UUID format")
        @Schema(description = "Customer reference in UUID format", example = "550e8400-e29b-41d4-a716-446655440000")
        String customerReference,

        @NotNull(message = "Order items cannot be null")
        @Schema(description = "List of items in the order")
        List<@Valid OrderItemRequestDto> orderItems

) {
}
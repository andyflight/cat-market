package com.example.catsmarket.presenter.dto.order;

import com.example.catsmarket.common.OrderStatus;
import com.example.catsmarket.presenter.dto.orderItem.OrderItemResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Schema(description = "Response containing order details")
public record OrderResponseDto (
        @Schema(description = "Unique order number in UUID format", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID orderNumber,

        @Schema(description = "Unique customer ID in UUID format", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID customerId,

        @Schema(description = "List of items in the order")
        List<OrderItemResponseDto> orderItems,

        @Schema(description = "Status of the order", example = "COMPLETED")
        OrderStatus status,

        @Schema(description = "Total amount of the order", example = "600.00")
        Double total,

        @Schema(description = "Date the order was created", example = "2024-12-19T10:15:30Z")
        Date createdAt
) {
}

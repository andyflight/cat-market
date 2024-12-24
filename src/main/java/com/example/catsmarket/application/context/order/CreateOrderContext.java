package com.example.catsmarket.application.context.order;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class CreateOrderContext {

    UUID customerReference;

    List<OrderItemContext> orderItems;

}

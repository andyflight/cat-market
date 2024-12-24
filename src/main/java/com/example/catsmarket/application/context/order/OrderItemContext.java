package com.example.catsmarket.application.context.order;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OrderItemContext {

    String productCode;
    Integer quantity;
    Double currentPrice;

}

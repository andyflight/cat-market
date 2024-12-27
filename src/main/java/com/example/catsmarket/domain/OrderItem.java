package com.example.catsmarket.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class OrderItem {
    UUID id;
    Product product;
    Integer quantity;
    Double oldProductPrice;
}

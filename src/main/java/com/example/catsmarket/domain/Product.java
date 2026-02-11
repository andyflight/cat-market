package com.example.catsmarket.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Product {
    UUID id;
    String code;
    String name;
    String description;
    Double price;
    List<Category> categories;
}

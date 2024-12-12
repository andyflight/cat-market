package com.example.catsmarket.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class Product {
    Long id;
    String code;
    String name;
    String description;
    Double price;
    List<Category> categories;
}

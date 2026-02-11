package com.example.catsmarket.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Category {
    UUID id;
    String name;
}

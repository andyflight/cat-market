package com.example.catsmarket.presenter.dto.product;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
public record ProductResponseDto(
        String code,
        String name,
        String description,
        Double price,
        List<String> categoryNames
) {
}

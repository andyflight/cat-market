package com.example.catsmarket.presenter.dto.product;

import com.example.catsmarket.presenter.validation.ExtendedValidation;
import com.example.catsmarket.presenter.validation.ValidGalacticProductName;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@Builder
@Jacksonized
@GroupSequence({ProductRequestDto.class, ExtendedValidation.class})
public record ProductRequestDto(
        @NotNull(message = "Product code cannot be null")
        @Pattern(regexp = "\\d{12,13}", message = "Product code supports only upc/ean format")
        String code,
        @NotBlank(message = "Name cannot be blank")
        @ValidGalacticProductName(groups = ExtendedValidation.class)
        @Size(max=100, message = "Name cannot be longer than 100 characters")
        String name,
        @NotBlank(message = "Description cannot be blank")
        @Size(max=255, message = "Description cannot be longer than 255 characters")
        String description,
        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than zero")
        Double price,
        @NotNull
        List<String> categoryNames
) {
}

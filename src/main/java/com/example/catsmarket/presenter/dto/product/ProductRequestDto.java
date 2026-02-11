package com.example.catsmarket.presenter.dto.product;

import com.example.catsmarket.presenter.validation.ExtendedValidation;
import com.example.catsmarket.presenter.validation.ValidGalacticProductName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@Builder
@Jacksonized
@Schema(description = "Request to create or update a product")
@GroupSequence({ ProductRequestDto.class, ExtendedValidation.class})
public record ProductRequestDto(
        @NotNull(message = "Product code cannot be null")
        @Pattern(regexp = "\\d{12,13}", message = "Product code supports only upc/ean format")
        @Schema(description = "UPC/EAN code of the product", example = "123456789012")
        String code,

        @ValidGalacticProductName(groups = ExtendedValidation.class)
        @NotBlank(message = "Name cannot be blank")
        @Size(max=100, message = "Name cannot be longer than 100 characters")
        @Schema(description = "Name of the product. Should contain Nebula|Star|Cat|Cosmo|Galactic wod", example = "Star catphone")
        String name,

        @NotBlank(message = "Description cannot be blank")
        @Size(max=255, message = "Description cannot be longer than 255 characters")
        @Schema(description = "Description of the product", example = "Flagship model from Andromeda galaxy")
        String description,

        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be greater than zero")
        @Schema(description = "Price of the product", example = "69.99")
        Double price,

        @NotNull
        @Schema(description = "List of category names the product belongs to")
        List<String> categoryNames
) {
}
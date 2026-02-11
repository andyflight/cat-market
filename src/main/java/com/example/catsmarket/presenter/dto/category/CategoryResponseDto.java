package com.example.catsmarket.presenter.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Schema(description = "Response containing the category details")
public record CategoryResponseDto(
        @Schema(description = "Name of the category", example = "Electronics")
        String name
) {
}
package com.example.catsmarket.presenter.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Builder
@Jacksonized
@Schema(description = "Response containing customer details")
public record CustomerResponseDto(
        @Schema(description = "Unique reference for the customer", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID customerReference,

        @Schema(description = "Full name of the customer", example = "John Doe")
        String fullName,

        @Schema(description = "Username of the customer", example = "johndoe")
        String username,

        @Schema(description = "Email address of the customer", example = "johndoe@example.com")
        String email
) {}
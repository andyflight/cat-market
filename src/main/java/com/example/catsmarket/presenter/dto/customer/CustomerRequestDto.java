package com.example.catsmarket.presenter.dto.customer;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Schema(description = "Request to create a new customer")
public record CustomerRequestDto(
        @NotBlank(message = "Full name cannot be blank")
        @Schema(description = "Full name of the customer", example = "John Doe")
        String fullName,

        @NotBlank(message = "Username cannot be blank")
        @Schema(description = "Username for the customer's account", example = "johndoe123")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Schema(description = "Password for the customer's account", example = "securePassword123")
        String password,

        @NotNull(message = "Email cannot be null")
        @Email(message = "Email should be valid")
        @Schema(description = "Email address of the customer", example = "johndoe@example.com")
        String email
) {}
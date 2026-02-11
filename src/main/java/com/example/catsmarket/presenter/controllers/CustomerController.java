package com.example.catsmarket.presenter.controllers;

import com.example.catsmarket.application.CustomerService;
import com.example.catsmarket.presenter.dto.SimpleResponse;
import com.example.catsmarket.presenter.dto.customer.CustomerRequestDto;
import com.example.catsmarket.presenter.dto.customer.CustomerResponseDto;
import com.example.catsmarket.presenter.mapper.DtoCustomerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "API for managing customers")
public class CustomerController {

    private final CustomerService customerService;
    private final DtoCustomerMapper dtoCustomerMapper;

    @Operation(
            summary = "Create a new customer",
            description = "This endpoint creates a new customer in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data for creating a customer",
                    required = true
            )
    )
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid CustomerRequestDto request) {
        CustomerResponseDto response = dtoCustomerMapper.toDto(
                customerService.createCustomer(
                        dtoCustomerMapper.toContext(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update customer information",
            description = "This endpoint updates a customer's information based on their unique reference ID.",
            parameters = @Parameter(name = "reference", description = "Unique reference ID of the customer", required = true)
    )
    @PutMapping("/{reference}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable UUID reference,
                                                              @RequestBody @Valid CustomerRequestDto request) {
        CustomerResponseDto response = dtoCustomerMapper.toDto(
                customerService.updateCustomer(
                        reference,
                        dtoCustomerMapper.toContext(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve customer by reference ID",
            description = "This endpoint fetches customer details by their unique reference ID.",
            parameters = @Parameter(name = "reference", description = "Unique reference ID of the customer", required = true)
    )
    @GetMapping("/{reference}")
    public ResponseEntity<CustomerResponseDto> getCustomerByReference(@PathVariable UUID reference) {
        CustomerResponseDto response = dtoCustomerMapper.toDto(
                customerService.getByReference(reference)
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a customer",
            description = "This endpoint deletes a customer based on their unique reference ID.",
            parameters = @Parameter(name = "reference", description = "Unique reference ID of the customer", required = true)
    )
    @DeleteMapping("/{reference}")
    public ResponseEntity<SimpleResponse> deleteCustomer(@PathVariable UUID reference) {
        customerService.deleteCustomer(reference);

        return ResponseEntity.ok(new SimpleResponse("Successful"));
    }
}
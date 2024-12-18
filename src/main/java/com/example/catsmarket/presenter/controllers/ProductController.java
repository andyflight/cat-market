package com.example.catsmarket.presenter.controllers;

import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.presenter.dto.SimpleResponse;
import com.example.catsmarket.presenter.dto.product.ProductRequestDto;
import com.example.catsmarket.presenter.dto.product.ProductResponseDto;
import com.example.catsmarket.presenter.mapper.DtoProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "API for managing products")
public class ProductController {
    private final ProductService productService;
    private final DtoProductMapper dtoProductMapper;

    @Operation(
            summary = "Retrieve product by UPC",
            description = "This endpoint fetches a product based on its unique UPC code.",
            parameters = @Parameter(name = "code", description = "The unique UPC code of the product", required = true)
    )
    @GetMapping("/{code}")
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) {
        ProductResponseDto response = dtoProductMapper.toDto(productService.getProductByCode(code));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve all products",
            description = "This endpoint returns a list of all available products."
    )
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = dtoProductMapper.toDto(
                productService.getProducts()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve products by category",
            description = "This endpoint fetches all products that belong to a specific category.",
            parameters = @Parameter(name = "categoryName", description = "The name of the category", required = false)
    )
    @GetMapping(params={"categoryName"})
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategoryName(
            @RequestParam(value = "categoryName", required = false) String categoryName
    ) {
        List<ProductResponseDto> response = dtoProductMapper.toDto(
                productService.getProductsByCategory(categoryName)
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create a new product",
            description = "This endpoint creates a new product in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the product to create",
                    required = true
            )
    )
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto request) {
        ProductResponseDto response = dtoProductMapper.toDto(
                productService.createProduct(
                        dtoProductMapper.toContext(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update product details",
            description = "This endpoint updates the details of an existing product.",
            parameters = @Parameter(name = "code", description = "The unique UPC code of the product", required = true)
    )
    @PutMapping("/{code}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable String code,
            @RequestBody @Valid ProductRequestDto request) {
        ProductResponseDto response = dtoProductMapper.toDto(
                productService.updateProduct(
                        code,
                        dtoProductMapper.toContext(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a product",
            description = "This endpoint deletes a product based on its unique UPC code.",
            parameters = @Parameter(name = "code", description = "The unique UPC code of the product", required = true)
    )
    @DeleteMapping("/{code}")
    public ResponseEntity<SimpleResponse> deleteProduct(@PathVariable String code) {
        productService.deleteProduct(code);

        return ResponseEntity.ok(new SimpleResponse("Successful"));
    }

    @Operation(
            summary = "Retrieve all products with discount",
            description = "This endpoint returns a list of all products with applied discount"
    )
    @GetMapping("/discount")
    public ResponseEntity<List<ProductResponseDto>> getDiscountedProducts() {
        List<ProductResponseDto> response = dtoProductMapper.toDto(productService.getDiscountedProducts());

        return ResponseEntity.ok(response);
    }

}

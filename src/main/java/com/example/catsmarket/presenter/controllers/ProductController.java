package com.example.catsmarket.presenter.controllers;

import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.presenter.dto.SimpleResponse;
import com.example.catsmarket.presenter.dto.product.ProductRequestDto;
import com.example.catsmarket.presenter.dto.product.ProductResponseDto;
import com.example.catsmarket.presenter.mapper.DtoProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final DtoProductMapper dtoProductMapper;

    @GetMapping("/{code}")
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) {
        log.info("Getting product by code {}", code);
        ProductResponseDto response = dtoProductMapper.toDto(productService.getProductByCode(code));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        log.info("Getting all products");
        List<ProductResponseDto> response = dtoProductMapper.toDto(
                productService.getProducts()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping(params={"categoryName"})
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategoryName(
            @RequestParam(value = "categoryName", required = false) String categoryName
    ) {
        log.info("Getting all products by category name {}", categoryName);
        List<ProductResponseDto> response = dtoProductMapper.toDto(
                productService.getProductsByCategory(categoryName)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto request) {
        log.info("Creating new product ");
        ProductResponseDto response = dtoProductMapper.toDto(
                productService.createProduct(
                        dtoProductMapper.toContext(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable String code,
            @RequestBody @Valid ProductRequestDto request) {
        log.info("Updating product with code {}", code);
        ProductResponseDto response = dtoProductMapper.toDto(
                productService.updateProduct(
                        code,
                        dtoProductMapper.toContext(request)
                )
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<SimpleResponse> deleteProduct(@PathVariable String code) {
        log.info("Deleting product with code {}", code);
        productService.deleteProduct(code);

        return ResponseEntity.ok(new SimpleResponse("Successful"));
    }

    @GetMapping("/discount")
    public ResponseEntity<List<ProductResponseDto>> getDiscountedProducts() {
        List<ProductResponseDto> response = dtoProductMapper.toDto(productService.getDiscountedProducts());

        return ResponseEntity.ok(response);
    }

}

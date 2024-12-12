package com.example.catsmarket.presenter.controllers;

import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.presenter.dto.SimpleResponse;
import com.example.catsmarket.presenter.dto.product.ProductRequestDto;
import com.example.catsmarket.presenter.dto.product.ProductResponseDto;
import com.example.catsmarket.presenter.mapper.DtoProductMapper;
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
public class ProductController {
    private final ProductService productService;
    private final DtoProductMapper dtoProductMapper;

    @GetMapping("/{code}")
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) {
        ProductResponseDto response = dtoProductMapper.toDto(productService.getProductByCode(code));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = dtoProductMapper.toDto(
                productService.getProducts()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping(params={"categoryName"})
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByCategoryName(
            @RequestParam(value = "categoryName", required = false) String categoryName
    ) {
        List<ProductResponseDto> response = dtoProductMapper.toDto(
                productService.getProductsByCategory(categoryName)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto request) {
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
        productService.deleteProduct(code);

        return ResponseEntity.ok(new SimpleResponse("Successful"));
    }

}

package com.example.catsmarket.presenter.controllers;

import com.example.catsmarket.application.CategoryService;
import com.example.catsmarket.presenter.dto.category.CategoryResponseDto;
import com.example.catsmarket.presenter.mapper.DtoCategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
@Tag(name = "Categories", description = "API for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final DtoCategoryMapper dtoCategoryMapper;

    @Operation(
            summary = "Retrieve all categories",
            description = "This endpoint returns a list of all available categories."
    )
    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {

        List<CategoryResponseDto> response = dtoCategoryMapper.toDto(categoryService.getAllCategories());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Retrieve category by name",
            description = "This endpoint fetches a category based on its name.",
            parameters = @Parameter(name = "name", description = "The name of the category to retrieve", required = true)
    )
    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable String name) {

        CategoryResponseDto response = dtoCategoryMapper.toDto(categoryService.getByName(name));

        return ResponseEntity.ok(response);
    }

}
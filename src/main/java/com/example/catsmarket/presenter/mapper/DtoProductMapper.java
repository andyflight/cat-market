package com.example.catsmarket.presenter.mapper;

import com.example.catsmarket.application.context.product.ProductContext;
import com.example.catsmarket.domain.Product;
import com.example.catsmarket.domain.Category;
import com.example.catsmarket.presenter.dto.product.ProductRequestDto;
import com.example.catsmarket.presenter.dto.product.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoProductMapper {


    ProductContext toContext(ProductRequestDto dto);

    @Mapping(target="categoryNames", source="categories")
    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDto(List<Product> products);

    default List<String> mapCategoriesToNames(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getName)
                .toList();
    }

}

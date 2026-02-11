package com.example.catsmarket.presenter.mapper;

import com.example.catsmarket.domain.Category;
import com.example.catsmarket.presenter.dto.category.CategoryResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DtoCategoryMapper {

    CategoryResponseDto toDto(Category category);

    List<CategoryResponseDto> toDto(List<Category> categoryList);

}
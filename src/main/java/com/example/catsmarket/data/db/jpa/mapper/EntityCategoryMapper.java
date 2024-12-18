package com.example.catsmarket.data.db.jpa.mapper;

import com.example.catsmarket.data.db.jpa.entity.CategoryEntity;
import com.example.catsmarket.domain.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityCategoryMapper {

    Category toDomain(CategoryEntity entity);

    List<Category> toDomain(List<CategoryEntity> entities);

}

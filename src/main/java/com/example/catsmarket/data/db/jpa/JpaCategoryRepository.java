package com.example.catsmarket.data.db.jpa;

import com.example.catsmarket.data.db.jpa.entity.CategoryEntity;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface JpaCategoryRepository extends NaturalRepository<CategoryEntity, String> {

}

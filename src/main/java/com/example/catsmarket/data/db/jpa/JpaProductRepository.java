package com.example.catsmarket.data.db.jpa;

import com.example.catsmarket.data.db.jpa.projection.ProductIdProjection;
import com.example.catsmarket.data.db.jpa.entity.ProductEntity;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Hidden
public interface JpaProductRepository extends NaturalRepository<ProductEntity, String> {

    List<ProductEntity> findAllByCategories_Name(String categoryName);

    Optional<ProductIdProjection> findProductByCode(String code);

    @Query("SELECT p FROM ProductEntity p LEFT JOIN FETCH p.categories WHERE p.code = :naturalId")
    Optional<ProductEntity> findByNaturalId(String naturalId);
}

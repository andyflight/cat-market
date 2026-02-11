package com.example.catsmarket.data.db.jpa.mapper;

import com.example.catsmarket.domain.Product;
import com.example.catsmarket.data.db.jpa.entity.ProductEntity;
import java.util.List;

import jakarta.persistence.PersistenceUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import static jakarta.persistence.Persistence.getPersistenceUtil;

@Mapper(componentModel = "spring")
public interface EntityProductMapper {

    @Mapping(target = "categories", ignore = true)
    Product toDomain(ProductEntity entity);

    List<Product> toDomain(List<ProductEntity> entities);

    ProductEntity toEntity(Product domain);

    @AfterMapping
    default void afterMapping(ProductEntity entity, @MappingTarget Product.ProductBuilder target) {
        PersistenceUtil util = getPersistenceUtil();

        if (util.isLoaded(entity, "categories")) {
            target.categories(Mappers.getMapper(EntityCategoryMapper.class).toDomain(entity.getCategories()));
        }
    }

}

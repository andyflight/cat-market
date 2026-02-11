package com.example.catsmarket.data.db.jpa.impl;

import com.example.catsmarket.data.db.jpa.projection.ProductIdProjection;
import com.example.catsmarket.data.ProductRepository;
import com.example.catsmarket.data.db.jpa.JpaProductRepository;
import com.example.catsmarket.data.db.jpa.mapper.EntityProductMapper;
import com.example.catsmarket.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final EntityProductMapper entityProductMapper;

    @Override
    public Product save(Product product) {

        return entityProductMapper.toDomain(
                jpaProductRepository.save(
                        entityProductMapper.toEntity(product)
                )
        );
    }

    @Override
    public Optional<Product> findByCode(String code){
        return Optional.ofNullable(entityProductMapper.toDomain(
                jpaProductRepository.findByNaturalId(code).orElse(null)
        ));
    }

    @Override
    public List<Product> findAll() {
        return entityProductMapper.toDomain(
                jpaProductRepository.findAll()
        );
    }

    @Override
    public List<Product> findByCategoryName(String categoryName) {
        return entityProductMapper.toDomain(
                jpaProductRepository.findAllByCategories_Name(categoryName)
        );
    }

    @Override
    public void deleteByCode(String code) {
        jpaProductRepository.deleteByNaturalId(code);
    }

    @Override
    public void deleteAll() {
        jpaProductRepository.deleteAll();
    }

    @Override
    public Optional<Product> findProductIdProjection(String code){
        Optional<ProductIdProjection> projection =  jpaProductRepository.findProductByCode(code);

        return projection.map(
                proj -> Product.builder().id(proj.getId()).code(code).build()
        );
    }

}

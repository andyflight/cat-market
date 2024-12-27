package com.example.catsmarket.data;

import com.example.catsmarket.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    List<Product> findAll();

    List<Product> findByCategoryName(String name);

    Optional<Product> findByCode(String code);

    void deleteByCode(String code);

    void deleteAll();

}

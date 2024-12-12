package com.example.catsmarket.data;

import com.example.catsmarket.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findAll();

    List<Category> findAllByName(List<String> names);

    Optional<Category> findByName(String name);

}

package com.example.catsmarket.data.mock.impl;


import com.example.catsmarket.domain.Category;
import com.example.catsmarket.data.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryCategoryRepositoryImpl implements CategoryRepository {

    private final Map<String, Category> categoryMap;

    public InMemoryCategoryRepositoryImpl() {
        this.categoryMap = Map.of(
                "Food", Category.builder().name("Food").build(),
                "Furniture", Category.builder().name("Furniture").build(),
                "Toys", Category.builder().name("Toys").build(),
                "Accessories", Category.builder().name("Accessories").build(),
                "Health", Category.builder().name("Health").build()
        );
    }

    @Override
    public List<Category> findAll() {
        return categoryMap.values().stream().toList();
    }

    @Override
    public List<Category> findAllByName(List<String> names) {
        if (names == null || names.isEmpty()) {
            return List.of();
        }
        return categoryMap.values().stream()
                .filter(category -> names.contains(category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryMap.values().stream()
                .filter(category -> category.getName().equalsIgnoreCase(name))
                .findFirst();
    }

}

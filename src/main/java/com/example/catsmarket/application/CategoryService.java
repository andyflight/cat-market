package com.example.catsmarket.application;

import com.example.catsmarket.domain.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getByName(String categoryName);

    List<Category> getAllCategoriesByNames(List<String> names);

}

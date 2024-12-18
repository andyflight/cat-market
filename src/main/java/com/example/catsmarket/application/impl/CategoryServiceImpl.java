package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.CategoryService;
import com.example.catsmarket.application.exceptions.CategoryNotFoundException;
import com.example.catsmarket.application.exceptions.CategoryPartialResultException;
import com.example.catsmarket.domain.Category;
import com.example.catsmarket.data.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new CategoryNotFoundException(name));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategoriesByNames(List<String> names) {
        List<Category> categories = categoryRepository.findAllByName(names);

        List<String> notFoundCategories = names.stream()
                .filter(n -> !categories.stream().map(Category::getName).toList().contains(n))
                .toList();

        if (!notFoundCategories.isEmpty()) {
            throw new CategoryPartialResultException(notFoundCategories.toString());
        }

        return categories;
    }

}

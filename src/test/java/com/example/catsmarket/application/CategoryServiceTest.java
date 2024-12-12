package com.example.catsmarket.application;

import com.example.catsmarket.application.exceptions.CategoryNotFoundException;
import com.example.catsmarket.application.exceptions.CategoryPartialResultException;
import com.example.catsmarket.data.CategoryRepository;
import com.example.catsmarket.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@DisplayName("Category Service Test")
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;


    @Test
    void getAllCategories_shouldReturnAllCategories() {

        List<Category> expectedCategories = List.of(
                Category.builder().id(1L).name("Food").build(),
                Category.builder().id(2L).name("Toys").build(),
                Category.builder().id(3L).name("Furniture").build()
        );

        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        List<Category> actualCategories = categoryService.getAllCategories();

        assertEquals(expectedCategories.size(), actualCategories.size());
        assertIterableEquals(expectedCategories, actualCategories);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getByName_shouldReturnCategory_WhenCategoryExists() {
        String categoryName = "Food";
        Category expectedCategory = Category.builder().id(1L).name(categoryName).build();

        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(expectedCategory));

        Category actualCategory = categoryService.getByName(categoryName);

        assertNotNull(actualCategory);
        assertEquals(categoryName, actualCategory.getName());
        verify(categoryRepository, times(1)).findByName(categoryName);
    }


    @Test
    void GetByName_shouldThrowException_WhenCategoryNotFound() {
        String categoryName = "NonExistingCategory";
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getByName(categoryName));
        verify(categoryRepository, times(1)).findByName(categoryName);

    }

    @Test
    void getAllCategoriesByNames_shouldReturnAllCategories_WhenAllExist() {
        List<String> categoryNames = List.of("Food", "Toys", "Furniture");
        List<Category> expectedCategories = List.of(
                Category.builder().id(1L).name("Food").build(),
                Category.builder().id(2L).name("Toys").build(),
                Category.builder().id(3L).name("Furniture").build()
        );

        when(categoryRepository.findAllByName(categoryNames)).thenReturn(expectedCategories);
        List<Category> actualCategories = categoryService.getAllCategoriesByNames(categoryNames);
        assertEquals(expectedCategories.size(), actualCategories.size());
        assertIterableEquals(expectedCategories, actualCategories);
        verify(categoryRepository, times(1)).findAllByName(categoryNames);

    }

    @Test
    void getAllCategoriesByNames_shouldThrowException_WhenSomeDoNotExist() {
        List<String> categoryNames = List.of("Food", "Toys", "Furniture");
        List<Category> categories = List.of(
                Category.builder().id(1L).name("Food").build(),
                Category.builder().id(3L).name("Furniture").build()
        );

        when(categoryRepository.findAllByName(categoryNames)).thenReturn(categories);
        CategoryPartialResultException ex = assertThrows(CategoryPartialResultException.class, () -> categoryService.getAllCategoriesByNames(categoryNames));
        assertTrue(ex.getMessage().contains("Toys"));
        verify(categoryRepository, times(1)).findAllByName(categoryNames);

    }

    @Test
    void getAllCategoriesByNames_shouldThrowException_WhenNamesIsEmpty() {
        List<String> categoryNames = List.of();

        when(categoryRepository.findAllByName(categoryNames)).thenReturn(List.of());
        List<Category> actualCategories = categoryService.getAllCategoriesByNames(categoryNames);

        assertTrue(actualCategories.isEmpty());
        verify(categoryRepository, times(1)).findAllByName(categoryNames);

    }

}

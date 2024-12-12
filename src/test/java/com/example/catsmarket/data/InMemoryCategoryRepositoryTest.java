package com.example.catsmarket.data;

import com.example.catsmarket.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InMemoryCategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;



    @Test
    void findAll_ShouldReturnAllCategories() {

        List<Category> expectedCategories = List.of(
                Category.builder().name("Furniture").build(),
                Category.builder().name("Food").build(),
                Category.builder().name("Toys").build(),
                Category.builder().name("Accessories").build(),
                Category.builder().name("Health").build()
        );

        List<Category> actualCategories = categoryRepository.findAll();

        assertEquals(expectedCategories.size(), actualCategories.size());
        assertTrue(expectedCategories.containsAll(actualCategories) && actualCategories.containsAll(expectedCategories));
    }

    @Test
    void findAllByName_ShouldReturnCategories_WhenAllNamesExist() {
        List<String> names = List.of("Food", "Furniture");

        List<Category> expectedCategories = List.of(
                Category.builder().name("Furniture").build(),
                Category.builder().name("Food").build()
        );

        List<Category> actualCategories = categoryRepository.findAllByName(names);

        assertEquals(expectedCategories.size(), actualCategories.size());
        assertTrue(expectedCategories.containsAll(actualCategories) && actualCategories.containsAll(expectedCategories));

    }

    @Test
    void findAllByName_ShouldReturnSomeCategories_WhenNotAllNamesExist() {
        List<String> names = List.of("Food", "non-existing");

        List<Category> expectedCategories = List.of(
                Category.builder().name("Food").build()
        );

        List<Category> actualCategories = categoryRepository.findAllByName(names);

        assertEquals(expectedCategories.size(), actualCategories.size());
        assertIterableEquals(expectedCategories, actualCategories);
    }

    @Test
    void findAllByName_ShouldReturnEmpty_WhenNamesDoNotExist() {
        List<String> names = List.of("non-existing");

        List<Category> expectedCategories = List.of();

        List<Category> actualCategories = categoryRepository.findAllByName(names);

        assertEquals(0, actualCategories.size());
        assertIterableEquals(expectedCategories, actualCategories);

    }

    @Test
    void findByName_ShouldReturnCategory_WhenNameExists() {
        String name = "Food";

        Category expectedCategory = Category.builder().name(name).build();

        Optional<Category> actualCategory = categoryRepository.findByName(name);

        assertTrue(actualCategory.isPresent());
        assertEquals(expectedCategory, actualCategory.get());

    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNameDoesNotExist() {
        String name = "non-existing";
        Optional<Category> actualCategory = categoryRepository.findByName(name);

        assertTrue(actualCategory.isEmpty());
    }

}

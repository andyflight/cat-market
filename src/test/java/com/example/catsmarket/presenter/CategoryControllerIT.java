package com.example.catsmarket.presenter;

import com.example.catsmarket.AbstractIT;
import com.example.catsmarket.application.CategoryService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Category Controller IT")
public class CategoryControllerIT extends AbstractIT {


    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private CategoryService categoryService;

    @BeforeEach
    void setup() {
        reset(categoryService);
    }

    @Test
    @SneakyThrows
    void getAllCategories_ShouldReturnAllCategories() {
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[*].name").exists());
    }

    @Test
    @SneakyThrows
    void getCategoryByName_ShouldReturnCategory_WhenCategoryExists() {
        mockMvc.perform(get("/api/v1/categories/food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Food")));
    }

    @Test
    @SneakyThrows
    void getCategoryById_ShouldThrowException_WhenCategoryDoesNotExist() {
        mockMvc.perform(get("/api/v1/categories/non-existing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("category-not-found")))
                .andExpect(jsonPath("$.title", is("Category Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Category with name non-existing not found")))
                .andExpect(jsonPath("$.instance", is("/api/v1/categories/non-existing")));

    }
}

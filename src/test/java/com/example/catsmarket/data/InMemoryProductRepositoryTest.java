package com.example.catsmarket.data;

import com.example.catsmarket.domain.Category;
import com.example.catsmarket.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("InMemory Product Repository Test")
public class InMemoryProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productRepository.save(Product.builder()
                .code("123456789012")
                .name("cat food")
                .description("cat food")
                .price(5.00)
                .categories(List.of(Category.builder().name("Food").build()))
                .build());

        productRepository.save(Product.builder()
                .code("123456789013")
                .name("cat toy")
                .description("cat toy")
                .price(6.00)
                .categories(List.of(Category.builder().name("Toys").build()))
                .build());
    }

    @Test
    void save_ShouldReturnProduct() {
        Product product = Product.builder()
                .code("123456789014")
                .name("cat thing")
                .description("cat thing")
                .price(1.00)
                .categories(List.of())
                .build();

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct);
        assertEquals(product.getCode(), savedProduct.getCode());
        assertEquals(product.getName(), savedProduct.getName());
        assertEquals(product.getDescription(), savedProduct.getDescription());
        assertEquals(product.getPrice(), savedProduct.getPrice());
        assertIterableEquals(product.getCategories(), savedProduct.getCategories());

    }

    @Test
    void findAll_ShouldReturnAll() {

        List<Product> expectedProducts = List.of(
                Product.builder()
                        .code("123456789012")
                        .name("cat food")
                        .description("cat food")
                        .price(5.00)
                        .categories(List.of(Category.builder().name("Food").build()))
                        .build(),
                Product.builder()
                        .code("123456789013")
                        .name("cat toy")
                        .description("cat toy")
                        .price(6.00)
                        .categories(List.of(Category.builder().name("Toys").build()))
                        .build()
        );

        List<Product> actualProducts = productRepository.findAll();

        assertEquals(expectedProducts.size(), actualProducts.size());
        assertIterableEquals(expectedProducts, actualProducts);
    }

    @Test
    void findByCode_ShouldReturnProduct_WhenProductExists() {
        String code = "123456789012";

        Product expectedProduct = Product.builder()
                .code(code)
                .name("cat food")
                .description("cat food")
                .price(5.00)
                .categories(List.of(Category.builder().name("Food").build()))
                .build();

        Optional<Product> actualProduct = productRepository.findByCode(code);

        assertTrue(actualProduct.isPresent());
        assertEquals(expectedProduct, actualProduct.get());
    }

    @Test
    void findByCode_ShouldReturnEmpty_WhenProductDoesNotExist() {
        String code = "non-existing code";

        Optional<Product> actualProduct = productRepository.findByCode(code);

        assertTrue(actualProduct.isEmpty());
    }

    @Test
    void findByCategoryName_ShouldReturnProduct_WhenCategoryExists() {
        String categoryName = "Food";

        List<Product> expectedProducts = List.of(
                Product.builder()
                        .code("123456789012")
                        .name("cat food")
                        .description("cat food")
                        .price(5.00)
                        .categories(List.of(Category.builder().name(categoryName).build()))
                        .build()
        );

        List<Product> actualProducts = productRepository.findByCategoryName(categoryName);

        assertEquals(expectedProducts.size(), actualProducts.size());
        assertIterableEquals(expectedProducts, actualProducts);


    }

    @Test
    void findByCategoryName_ShouldReturnEmpty_WhenCategoryDoesNotExist() {
        String categoryName = "non-existing category";

        List<Product> actualProducts = productRepository.findByCategoryName(categoryName);

        assertNotNull(actualProducts);
        assertTrue(actualProducts.isEmpty());

    }

    @Test
    void deleteByCode_ShouldDeleteProduct_WhenProductExists() {
        String code = "123456789012";
        productRepository.deleteByCode(code);

        Optional<Product> actualProduct = productRepository.findByCode(code);
        assertTrue(actualProduct.isEmpty());
    }

    @Test
    void deleteByCode_ShouldDoNothing_WhenProductDoesNotExist() {
        String code = "non-existing code";
        productRepository.deleteByCode(code);

        Optional<Product> actualProduct = productRepository.findByCode(code);
        assertTrue(actualProduct.isEmpty());
    }

    @Test
    void deleteAll_ShouldDeleteAll() {
        productRepository.deleteAll();

        List<Product> actualProducts = productRepository.findAll();
        assertTrue(actualProducts.isEmpty());
    }


}

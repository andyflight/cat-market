package com.example.catsmarket.application;

import com.example.catsmarket.application.context.product.ProductContext;
import com.example.catsmarket.application.context.price.PriceValidationContext;
import com.example.catsmarket.application.exceptions.PriceNotValidException;
import com.example.catsmarket.application.exceptions.ProductNotFoundException;
import com.example.catsmarket.data.ProductRepository;
import com.example.catsmarket.domain.Product;
import com.example.catsmarket.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Product Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PriceService priceService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    private final ProductContext productContext = ProductContext.builder()
            .code("123456789012")
            .name("cat product")
            .description("cat product description")
            .price(5.00)
            .categoryNames(List.of("Product"))
            .build();



    @Test
    void getAllProducts_ShouldReturnAllProducts() {
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
                        .name("cat cookies")
                        .description("cat cookies")
                        .price(5.00)
                        .categories(List.of(Category.builder().name("Food").build()))
                        .build(),
                Product.builder()
                        .code("123456789014")
                        .name("cat mint")
                        .description("cat mint")
                        .categories(List.of(Category.builder().name("Food").build()))
                        .price(5.00)
                        .build()
        );

        when(productRepository.findAll()).thenReturn(expectedProducts);
        List<Product> actualProducts = productService.getProducts();
        assertEquals(expectedProducts.size(), actualProducts.size());
        assertIterableEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getAllProductsByCategoryName_ShouldReturnProducts_WhenCategoryExists() {
        String categoryName = "Food";
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
                        .name("cat cookies")
                        .description("cat cookies")
                        .price(5.00)
                        .categories(List.of(Category.builder().name("Food").build()))
                        .build(),
                Product.builder()
                        .code("123456789014")
                        .name("cat mint")
                        .description("cat mint")
                        .categories(List.of(Category.builder().name("Food").build()))
                        .price(5.00)
                        .build()
        );

        when(productRepository.findByCategoryName(categoryName)).thenReturn(expectedProducts);
        List<Product> actualProducts = productService.getProductsByCategory(categoryName);
        assertEquals(expectedProducts.size(), actualProducts.size());
        assertIterableEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findByCategoryName(categoryName);
    }

    @Test
    void getAllProductsByCategoryName_ShouldReturnEmptyProducts_WhenCategoryDoesNotExist() {
        String categoryName = "Non-existent category";
        List<Product> expectedProducts = List.of();
        when(productRepository.findByCategoryName(categoryName)).thenReturn(expectedProducts);
        List<Product> actualProducts = productService.getProductsByCategory(categoryName);

        assertTrue(actualProducts.isEmpty());
        verify(productRepository, times(1)).findByCategoryName(categoryName);

    }


    @Test
    void getProductByCode_ShouldReturnProduct_WhenProductExists() {
        String code = "123456789012";
        Product expectedProduct = Product.builder()
                .code(code)
                .name("cat product")
                .description("cat product description")
                .price(5.00)
                .categories(List.of(Category.builder().name("Category").build()))
                .build();

        when(productRepository.findByCode(code)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.getProductByCode(code);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getCode(), actualProduct.getCode());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
        assertEquals(expectedProduct.getPrice(), actualProduct.getPrice());
        assertIterableEquals(expectedProduct.getCategories(), actualProduct.getCategories());
        verify(productRepository, times(1)).findByCode(code);
    }

    @Test
    void getProductByCode_ShouldThrowException_WhenProductDoesNotExist() {
        String code = "non-existent code";
        when(productRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductByCode(code));
        verify(productRepository, times(1)).findByCode(code);
    }

    @Test
    void createProduct_ShouldCreateProduct_WhenRequestValid() {

        Product expectedProduct = Product.builder()
                .code("123456789012")
                .name("cat product")
                .description("cat product description")
                .price(5.00)
                .categories(List.of(Category.builder().name("Product").build()))
                .build();

        PriceValidationContext priceContext = PriceValidationContext.builder().isValidated(true).build();

        when(priceService.checkValidation(productContext.getPrice())).thenReturn(priceContext);
        when(categoryService.getAllCategoriesByNames(productContext.getCategoryNames())).thenReturn(expectedProduct.getCategories());
        when(productRepository.save(productCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        Product actualProduct = productService.createProduct(productContext);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getCode(), actualProduct.getCode());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
        assertEquals(expectedProduct.getPrice(), actualProduct.getPrice());
        assertIterableEquals(expectedProduct.getCategories(), actualProduct.getCategories());
        verify(priceService, times(1)).checkValidation(productContext.getPrice());
        verify(categoryService, times(1)).getAllCategoriesByNames(productContext.getCategoryNames());
        verify(productRepository, times(1)).save(productCaptor.capture());

    }


    @Test
    void createProduct_ShouldThrowException_WhenPriceValidationFailed() {

        PriceValidationContext priceContext = PriceValidationContext.builder().isValidated(false).build();

        when(priceService.checkValidation(productContext.getPrice())).thenReturn(priceContext);

        assertThrows(PriceNotValidException.class, () -> productService.createProduct(productContext));
        verify(priceService, times(1)).checkValidation(productContext.getPrice());

    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenRequestValid() {
        String code = "123456789012";

        Product oldProduct = Product.builder()
                .code("12345678900")
                .name("cat old product")
                .description("cat old product description")
                .price(4.00)
                .categories(List.of())
                .build();

        Product expectedProduct = Product.builder()
                .code("12345678900")
                .name("cat product")
                .description("cat product description")
                .price(5.00)
                .categories(List.of(Category.builder().name("Product").build()))
                .build();

        PriceValidationContext priceContext = PriceValidationContext.builder().isValidated(true).build();

        when(priceService.checkValidation(productContext.getPrice())).thenReturn(priceContext);
        when(productRepository.findByCode(code)).thenReturn(Optional.of(oldProduct));
        when(categoryService.getAllCategoriesByNames(productContext.getCategoryNames())).thenReturn(expectedProduct.getCategories());
        when(productRepository.save(productCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        Product actualProduct = productService.updateProduct(code, productContext);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getCode(), actualProduct.getCode());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        assertEquals(expectedProduct.getDescription(), actualProduct.getDescription());
        assertEquals(expectedProduct.getPrice(), actualProduct.getPrice());
        assertIterableEquals(expectedProduct.getCategories(), actualProduct.getCategories());
        verify(priceService, times(1)).checkValidation(productContext.getPrice());
        verify(categoryService, times(1)).getAllCategoriesByNames(productContext.getCategoryNames());
        verify(productRepository, times(1)).save(productCaptor.capture());
        verify(productRepository, times(1)).findByCode(code);
    }



    @Test
    void updateProduct_ShouldThrowException_WhenPriceValidationFailed() {
        String code = "123456789012";
        PriceValidationContext priceContext = PriceValidationContext.builder().isValidated(false).build();
        when(priceService.checkValidation(productContext.getPrice())).thenReturn(priceContext);

        assertThrows(PriceNotValidException.class, () -> productService.updateProduct(code, productContext));
        verify(priceService, times(1)).checkValidation(productContext.getPrice());

    }

    @Test
    void deleteProduct_ShouldDeleteProduct_Anyway() {
        String code = "any code";
        doNothing().when(productRepository).deleteByCode(code);

        productService.deleteProduct(code);

        verify(productRepository, times(1)).deleteByCode(code);
    }


}

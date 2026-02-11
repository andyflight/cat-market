package com.example.catsmarket.presenter;

import com.example.catsmarket.AbstractIT;
import com.example.catsmarket.application.CategoryService;
import com.example.catsmarket.application.PriceService;
import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.application.context.price.PriceValidationResponse;
import com.example.catsmarket.common.FeatureName;
import com.example.catsmarket.data.ProductRepository;
import com.example.catsmarket.domain.Category;
import com.example.catsmarket.domain.Product;
import com.example.catsmarket.featuretoggle.FeatureToggleExtension;
import com.example.catsmarket.featuretoggle.annotation.DisableFeature;
import com.example.catsmarket.featuretoggle.annotation.EnableFeature;
import com.example.catsmarket.presenter.dto.product.ProductRequestDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@DisplayName("Product Controller IT")
@ExtendWith(FeatureToggleExtension.class)
public class ProductControllerIT extends AbstractIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Value("${application.features.discount.value}")
    private Double discountValue;

    @SpyBean
    private PriceService priceService;

    @SpyBean
    private ProductService productService;

    @SpyBean
    private CategoryService categoryService;

    private final ProductRequestDto validProductRequest = new ProductRequestDto(
            "123456789014",
            "Cat star food",
            "Cat star food",
            3.00,
            List.of("Food")
    );

    private final ProductRequestDto invalidProductRequest = new ProductRequestDto(
            null,
            null,
            null,
            null,
            null
    );

    private final ProductRequestDto invalidCategoriesProductRequest = new ProductRequestDto(
            "123456789014",
            "Cat star food",
            "Cat star food",
            3.00,
            List.of("I'm not exist")
    );

    private final ProductRequestDto invalidNameProductRequest = new ProductRequestDto(
            "123456789014",
            "I'm not valid",
            "Cat star food",
            3.00,
            List.of("Food")
    );

    @BeforeEach
    void setup() {
        reset(priceService, productService, categoryService);
        productRepository.deleteAll();
        productRepository.save(Product.builder()
                .code("123456789012")
                .name("cat food")
                .description("cat food")
                .price(5.00)
                .categories(List.of(Category.builder().id(1L).name("Food").build()))
                .build());

        productRepository.save(Product.builder()
                .code("123456789013")
                .name("cat toy")
                .description("cat toy")
                .price(6.00)
                .categories(List.of(Category.builder().id(51L).name("Toys").build()))
                .build());
    }

    @Test
    @SneakyThrows
    void getAllProducts_ShouldReturnAllProducts() {
        mockMvc.perform(get("/api/v1/products")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].code").exists())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].description").exists())
                .andExpect(jsonPath("$[*].price").exists())
                .andExpect(jsonPath("$[*].categoryNames").exists());

    }

    @Test
    @SneakyThrows
    void getAllProductsByCategoryName_ShouldReturnProducts_WhenCategoryExists() {
        mockMvc.perform(get("/api/v1/products?categoryName=Food")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].code").exists())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].description").exists())
                .andExpect(jsonPath("$[*].price").exists())
                .andExpect(jsonPath("$[*].categoryNames").exists());
    }

    @Test
    @SneakyThrows
    void getAllProductsByCategoryName_ShouldReturnEmptyProducts_WhenCategoryDoesNotExist() {
        mockMvc.perform(get("/api/v1/products?categoryName=smth")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    @SneakyThrows
    void getProductByCode_ShouldReturnProduct_WhenProductExists() {
        mockMvc.perform(get("/api/v1/products/123456789012")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("123456789012")))
                .andExpect(jsonPath("$.name", is("cat food")))
                .andExpect(jsonPath("$.description", is("cat food")))
                .andExpect(jsonPath("$.price", is(5.00)))
                .andExpect(jsonPath("$.categoryNames", contains("Food")));
    }

    @Test
    @SneakyThrows
    void getProductByCode_ShouldThrowException_WhenProductDoesNotExist() {

        mockMvc.perform(get("/api/v1/products/non-existing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("product-not-found")))
                .andExpect(jsonPath("$.title", is("Product Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Product with code non-existing not found")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products/non-existing")));

    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldCreateProduct_WhenRequestValid() {


        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(true)))
                ));

        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(validProductRequest.code())))
                .andExpect(jsonPath("$.name", is(validProductRequest.name())))
                .andExpect(jsonPath("$.description", is(validProductRequest.description())))
                .andExpect(jsonPath("$.price", is(validProductRequest.price())))
                .andExpect(jsonPath("$.categoryNames", contains("Food")));
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldThrowException_WhenRequestNotValid() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(true)))
                ));

        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("urn:problem-type:validation-error")))
                .andExpect(jsonPath("$.title", is("Field Validation Exception")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Request validation failed")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products")))
                .andExpect(jsonPath("$.invalidParams[*].fieldName").exists())
                .andExpect((jsonPath("$.invalidParams[*].reason").exists()));

    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldThrowException_WhenNameNotValid() {

        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNameProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("urn:problem-type:validation-error")))
                .andExpect(jsonPath("$.title", is("Field Validation Exception")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Request validation failed")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products")))
                .andExpect(jsonPath("$.invalidParams[*].fieldName").exists())
                .andExpect((jsonPath("$.invalidParams[*].reason").exists()));

    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldThrowException_WhenPriceClientFailed() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(400)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                Map.of("title", "Bad Request")))
                ));

        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.type", is("price-client-failed")))
                .andExpect(jsonPath("$.title", is("External Price Service Unavailable")))
                .andExpect(jsonPath("$.status", is(503)))
                .andExpect(jsonPath("$.detail", is("Bad Request")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products")));
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldThrowException_WhenPriceNotValid() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(false)))
                ));

        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("price-not-valid")))
                .andExpect(jsonPath("$.title", is("Price Validation Failed")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Price not valid: 3.0")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products")));
    }


    @Test
    @SneakyThrows
    @DisableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldCreateProduct_WhenRequestValidAndValidationDisabled() {
        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(validProductRequest.code())))
                .andExpect(jsonPath("$.name", is(validProductRequest.name())))
                .andExpect(jsonPath("$.description", is(validProductRequest.description())))
                .andExpect(jsonPath("$.price", is(validProductRequest.price())))
                .andExpect(jsonPath("$.categoryNames", contains("Food")));
    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureName.PRICE_VALIDATION)
    void createProduct_ShouldThrowException_WhenNotAllCategoriesExist() {
        mockMvc.perform(post("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoriesProductRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("category-partial-result")))
                .andExpect(jsonPath("$.title", is("Category Partial Result")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products")));
    }


    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void updateProduct_ShouldUpdateProduct_WhenRequestValid() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(true)))
                ));

        mockMvc.perform(put("/api/v1/products/123456789012")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validProductRequest.name())))
                .andExpect(jsonPath("$.description", is(validProductRequest.description())))
                .andExpect(jsonPath("$.price", is(validProductRequest.price())))
                .andExpect(jsonPath("$.categoryNames", contains("Food")));
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void updateProduct_ShouldThrowException_WhenRequestNotValid() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(true)))
                ));

        mockMvc.perform(put("/api/v1/products/123456789012")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("urn:problem-type:validation-error")))
                .andExpect(jsonPath("$.title", is("Field Validation Exception")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Request validation failed")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products/123456789012")))
                .andExpect(jsonPath("$.invalidParams[*].fieldName").exists())
                .andExpect((jsonPath("$.invalidParams[*].reason").exists()));

    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void updateProduct_ShouldThrowException_WhenPriceNotValid() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(false)))
                ));

        mockMvc.perform(put("/api/v1/products/123456789012")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("price-not-valid")))
                .andExpect(jsonPath("$.title", is("Price Validation Failed")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Price not valid: 3.0")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products/123456789012")));

    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.PRICE_VALIDATION)
    void updateProduct_ShouldThrowException_WhenProductDoesNotExist() {

        stubFor(WireMock.post("/api/v1/price/validation")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsBytes(
                                new PriceValidationResponse(true)))
                ));

        mockMvc.perform(get("/api/v1/products/non-existing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("product-not-found")))
                .andExpect(jsonPath("$.title", is("Product Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Product with code non-existing not found")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products/non-existing")));

    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureName.PRICE_VALIDATION)
    void updateProduct_ShouldUpdateProduct_WhenRequestValidAndValidationDisabled() {
        mockMvc.perform(put("/api/v1/products/123456789012")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(validProductRequest.name())))
                .andExpect(jsonPath("$.description", is(validProductRequest.description())))
                .andExpect(jsonPath("$.price", is(validProductRequest.price())))
                .andExpect(jsonPath("$.categoryNames", contains("Food")));
    }

    @Test
    @SneakyThrows
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {

        mockMvc.perform(delete("/api/v1/products/123456789012")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful")));
    }

    @Test
    @SneakyThrows
    void deleteProduct_ShouldDoNothing_WhenProductDoesNotExist() {
        mockMvc.perform(delete("/api/v1/products/non-existing")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful")));
    }

    @Test
    @SneakyThrows
    @EnableFeature(FeatureName.DISCOUNT)
    void getDiscountedProducts_ShouldReturnProducts_WhenDiscountEnabled() {

        mockMvc.perform(get("/api/v1/products/discount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].code").exists())
                .andExpect(jsonPath("$[*].name").exists())
                .andExpect(jsonPath("$[*].description").exists())
                .andExpect(jsonPath("$[*].price").exists())
                .andExpect(jsonPath("$[*].categoryNames").exists())
                 .andExpect(jsonPath("$[?(@.code == '123456789012')].price").value(5.00 * (1 - discountValue)))
                .andExpect(jsonPath("$[?(@.code == '123456789013')].price").value(6.00 * (1 - discountValue)));
    }

    @Test
    @SneakyThrows
    @DisableFeature(FeatureName.DISCOUNT)
    void getDiscountedProducts_ShouldThrowException_WhenFeatureDisabled() {

        mockMvc.perform(get("/api/v1/products/discount"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("feature-disabled")))
                .andExpect(jsonPath("$.title", is("Feature Disabled")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Feature discount is disabled")))
                .andExpect(jsonPath("$.instance", is("/api/v1/products/discount")));
    }



}

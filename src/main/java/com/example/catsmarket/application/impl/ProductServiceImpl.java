package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.CategoryService;
import com.example.catsmarket.application.PriceService;
import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.application.context.product.ProductContext;
import com.example.catsmarket.application.context.recommendation.PriceValidationContext;
import com.example.catsmarket.application.exceptions.PriceNotValidException;
import com.example.catsmarket.common.FeatureName;
import com.example.catsmarket.application.exceptions.ProductNotFoundException;
import com.example.catsmarket.domain.Category;
import com.example.catsmarket.domain.Product;
import com.example.catsmarket.data.ProductRepository;
import com.example.catsmarket.featuretoggle.FeatureToggleService;
import com.example.catsmarket.featuretoggle.annotation.FeatureToggle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final PriceService priceService;
    private final FeatureToggleService featureToggleService;


    @Override
    public Product createProduct(ProductContext productContext) {

        PriceValidationContext priceValidationContext = priceService.checkValidation(productContext.getPrice());

        if (priceValidationContext != null && Boolean.FALSE.equals(priceValidationContext.getIsValidated())) {
            log.error("Price validation check failed {}", productContext.getPrice());
            throw new PriceNotValidException(String.valueOf(productContext.getPrice()));
        }

        List<Category> categories = categoryService.getAllCategoriesByNames(productContext.getCategoryNames());

        Product product = Product.builder()
                .code(productContext.getCode())
                .name(productContext.getName())
                .description(productContext.getDescription())
                .price(productContext.getPrice())
                .categories(categories)
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String code, ProductContext productContext) {

        PriceValidationContext priceValidationContext = priceService.checkValidation(productContext.getPrice());

        if (priceValidationContext != null && Boolean.FALSE.equals(priceValidationContext.getIsValidated())) {
            log.error("Price validation check failed {}", productContext.getPrice());
            throw new PriceNotValidException(String.valueOf(productContext.getPrice()));
        }

        Product oldProduct = productRepository.findByCode(code)
                .orElseThrow(() -> {
                    log.error("Product with code {} not found", code);
                    return new ProductNotFoundException(code);
                });

        List<Category> categories = categoryService.getAllCategoriesByNames(productContext.getCategoryNames());

        Product newProduct = oldProduct.toBuilder()
                .code(productContext.getCode())
                .name(productContext.getName())
                .description(productContext.getDescription())
                .price(productContext.getPrice())
                .categories(categories)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductByCode(String code) {

        return productRepository.findByCode(code).orElseThrow(() -> {
            log.error("product with code {} not found", code);
            return new ProductNotFoundException(code);
        });
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        if (!categoryName.isBlank()) {
            return productRepository.findByCategoryName(categoryName);

        }
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(String code) {
        productRepository.deleteByCode(code);
    }

    @Override
    @FeatureToggle(value = FeatureName.DISCOUNT)
    public List<Product> getDiscountedProducts(){

        Double discount = featureToggleService.getFeatureValue(FeatureName.DISCOUNT.getDescription());

        return productRepository.findAll().stream()
                .map(
                    product -> product.toBuilder().price(product.getPrice() * (1 - discount)).build()
                ).toList();
    }

}

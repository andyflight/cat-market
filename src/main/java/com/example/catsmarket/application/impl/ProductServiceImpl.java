package com.example.catsmarket.application.impl;

import com.example.catsmarket.application.CategoryService;
import com.example.catsmarket.application.PriceService;
import com.example.catsmarket.application.ProductService;
import com.example.catsmarket.application.context.product.ProductContext;
import com.example.catsmarket.application.context.price.PriceValidationContext;
import com.example.catsmarket.application.exceptions.PriceNotValidException;
import com.example.catsmarket.application.exceptions.ProductNotFoundException;
import com.example.catsmarket.common.FeatureName;
import com.example.catsmarket.domain.Category;
import com.example.catsmarket.domain.Product;
import com.example.catsmarket.data.ProductRepository;
import com.example.catsmarket.featuretoggle.FeatureToggleService;
import com.example.catsmarket.featuretoggle.annotation.FeatureToggle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Product createProduct(ProductContext productContext) {

        PriceValidationContext priceValidationContext = priceService.checkValidation(productContext.getPrice());

        if (priceValidationContext != null && Boolean.FALSE.equals(priceValidationContext.getIsValidated())) {
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
    @Transactional()
    public Product updateProduct(String code, ProductContext productContext) {

        PriceValidationContext priceValidationContext = priceService.checkValidation(productContext.getPrice());

        if (priceValidationContext != null && Boolean.FALSE.equals(priceValidationContext.getIsValidated())) {
            throw new PriceNotValidException(String.valueOf(productContext.getPrice()));
        }

        Product oldProduct = productRepository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException(code));

        List<Category> categories = categoryService.getAllCategoriesByNames(productContext.getCategoryNames());

        Product newProduct = oldProduct.toBuilder()
                .name(productContext.getName())
                .description(productContext.getDescription())
                .price(productContext.getPrice())
                .categories(categories)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductByCode(String code) {
        return productRepository.findByCode(code).orElseThrow(() -> new ProductNotFoundException(code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String categoryName) {
        if (!categoryName.isBlank()) {
            return productRepository.findByCategoryName(categoryName);

        }
        return productRepository.findAll();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteProduct(String code) {
        productRepository.deleteByCode(code);
    }

    @Override
    @FeatureToggle(value = FeatureName.DISCOUNT)
    @Transactional(readOnly = true)
    public List<Product> getDiscountedProducts(){

        Double discount = featureToggleService.getFeatureValue(FeatureName.DISCOUNT.getDescription());

        return productRepository.findAll().stream()
                .map(
                    product -> product.toBuilder().price(product.getPrice() * (1 - discount)).build()
                ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductId(String code) {
        return productRepository.findProductIdProjection(code)
                .orElseThrow(() -> new ProductNotFoundException(code));
    }

}

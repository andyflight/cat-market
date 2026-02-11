package com.example.catsmarket.application;

import com.example.catsmarket.application.context.product.ProductContext;
import com.example.catsmarket.domain.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductContext context);

    Product updateProduct(String code, ProductContext context);

    Product getProductByCode(String code);

    List<Product> getProducts();

    List<Product> getProductsByCategory(String categoryName);

    void deleteProduct(String code);

    List<Product> getDiscountedProducts();

    Product getProductId(String code);
}

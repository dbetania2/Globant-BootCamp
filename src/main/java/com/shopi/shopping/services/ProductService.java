package com.shopi.shopping.services;

import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.factories.ProductFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductFactory productFactory;
    private final ProductRepository productRepository; // Adding the repository

    @Autowired
    public ProductService(ProductFactory productFactory, ProductRepository productRepository) {
        this.productFactory = productFactory;
        this.productRepository = productRepository; // Initializing the repository
    }

    // Method to create a product based on type
    public Product createProduct(String productType, String name, double price) {
        logger.info("Creating product with type: {}, name: {}, price: {}", productType, name, price);
        Product product = productFactory.createProduct(productType, name, price);
        return productRepository.save(product); // Saving the product to the repository
    }

    public Product getProductById(long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id).orElse(null); // Fetching product by ID
    }

    public void updateProduct(Product product) {
        logger.info("Updating product with ID: {}", product.getId());
        productRepository.save(product); // Updating the product in the repository
    }

    public void deleteProduct(long id) {
        logger.info("Deleting product with ID: {}", id);
        productRepository.deleteById(id); // Deleting the product by ID
    }


}



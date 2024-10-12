package com.shopi.shopping.services;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.factories.ProductFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

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
    public Product createProduct(String category, String name, BigDecimal price) {
        Product product = productFactory.createProduct(category, name, price);
        return productRepository.save(product);
    }

    // Method to get a product by its ID
    public Product getProductById(long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id).orElse(null); // Fetching product by ID
    }

    // ---- CRUD Methods Start Here ------------------------------------------------

    public void updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product does not exist");
        }
        productRepository.save(product);
    }

    public void deleteProduct(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product does not exist");
        }
        productRepository.deleteById(productId);
    }


    // Method to get all products
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll(); // Fetching all products
    }

    // Method to get products by name
    public List<Product> getProductsByName(String name) {
        logger.info("Fetching products with name: {}", name);
        return productRepository.findByName(name); // Fetching products by name
    }

    // Method to get products by category
    public List<Product> getProductsByType(Class<?> type) {
        return productRepository.findByProductType(type);
    }
}

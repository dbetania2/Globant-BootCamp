package com.shopi.shopping.services;
import com.shopi.shopping.models.products.Product;
import com.shopi.shopping.repositories.ProductRepository;
import com.shopi.shopping.factories.ProductFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductFactory productFactory;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductFactory productFactory, ProductRepository productRepository) {
        this.productFactory = productFactory;
        this.productRepository = productRepository; // Initialize repository
    }

    // Method to fetch product by ID without caching
    public Product getProductById(long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id).orElse(null); // Consider throwing an exception instead of returning null
    }

    // Method to fetch product by ID with caching
    @Cacheable(value = "products", key = "#id")
    public Product getProductByIdCached(long id) {
        logger.info("Fetching product with ID from repository: {}", id);
        return productRepository.findById(id).orElse(null);
    }


    // Method to update a product without cache eviction
    public void updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            throw new IllegalArgumentException("Product does not exist");
        }
        productRepository.save(product); // Update product in the repository
    }

    // Method to update a product with cache eviction
    @CacheEvict(value = "products", key = "#product.id") // Evict cached product
    public void updateProductWithCache(Product product) {
        updateProduct(product); // Call the method without caching
    }

    // Method to delete a product without cache eviction
    public void deleteProduct(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product does not exist");
        }
        productRepository.deleteById(productId); // Delete product from the repository
    }

    // Method to delete a product with cache eviction
    @CacheEvict(value = "products", key = "#productId") // Evict cached product
    public void deleteProductWithCache(long productId) {
        deleteProduct(productId); // Call the method without caching
    }

    // Method to get all products
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll(); // Fetch all products from the repository
    }

    // Method to get products by name
    public List<Product> getProductsByName(String name) {
        logger.info("Fetching products with name: {}", name);
        return productRepository.findByName(name); // Fetch products by name
    }

    // Method to get products by type
    public List<Product> getProductsByType(Class<?> type) {
        return productRepository.findByProductType(type); // Fetch products by type
    }

    // Method to create a product based on its type
    public Product createProduct(String category, String name, BigDecimal price) {
        Product product = productFactory.createProduct(category, name, price);
        return productRepository.save(product);
    }
}
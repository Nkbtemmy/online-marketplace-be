package com.imanzi.marketplace.service;

import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.Rating;
import com.imanzi.marketplace.model.Review;
import com.imanzi.marketplace.model.User;
import com.imanzi.marketplace.repository.*;
import com.imanzi.marketplace.util.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository, OrderRepository orderRepository, ReviewRepository reviewRepository, RatingRepository ratingRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        existingProduct.setName(product.getName() != null ? product.getName() : existingProduct.getName());
        existingProduct.setDescription(product.getDescription() !=null ? product.getDescription() : existingProduct.getDescription());
        existingProduct.setPrice(product.getPrice() != null ? product.getPrice() : existingProduct.getPrice());
        existingProduct.setCategory(product.getCategory() != null ? product.getCategory() : existingProduct.getCategory());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product markAsFeatured(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new UserException("PRODUCT_NOT_FOUND", "Product not found"));

        product.setFeatured(true);
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    public List<Product> getProductsByTag(String tag) {
        return productRepository.findByTag(tag);
    }

    public List<Product> searchProducts(String searchTerm) {
        return productRepository.searchByNameOrDescription(searchTerm);
    }

    public Review addReview(Long productId, String reviewText) {
        String userEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasUserOrderedProduct(user, product)) {
            throw new RuntimeException("User has not ordered this product");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .reviewText(reviewText)
                .build();

        return reviewRepository.save(review);
    }

    public Rating addRating(Long productId, int rating) {
        String userEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!hasUserOrderedProduct(user, product)) {
            throw new RuntimeException("User has not ordered this product");
        }

        Rating newRating = Rating.builder()
                .user(user)
                .product(product)
                .rating(rating)
                .build();

        return ratingRepository.save(newRating);
    }

    private boolean hasUserOrderedProduct(User user, Product product) {
        return orderRepository.findByUser(user).stream()
                .flatMap(order -> order.getProducts().stream())
                .anyMatch(orderedProduct -> orderedProduct.equals(product));
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}

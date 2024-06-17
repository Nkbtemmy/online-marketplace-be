package com.imanzi.marketplace.controller;

import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.Rating;
import com.imanzi.marketplace.model.Review;
import com.imanzi.marketplace.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marketplace/products")
@Tag(name = "Product Management", description = "Operations related to product management")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }


    @PutMapping("/{id}/feature")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> markProductAsFeatured(@PathVariable Long id) {
        Product updatedProduct = productService.markAsFeatured(id);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/category")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/tags")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<List<Product>> getProductsByTag(@RequestParam String tag) {
        List<Product> products = productService.getProductsByTag(tag);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{productId}/reviews")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<Review> addReview(@PathVariable Long productId, @RequestBody String reviewText) {
        Review review = productService.addReview(productId, reviewText);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/{productId}/ratings")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<Rating> addRating(@PathVariable Long productId, @RequestBody int rating) {
        Rating newRating = productService.addRating(productId, rating);
        return ResponseEntity.ok(newRating);
    }
}

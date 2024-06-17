package com.imanzi.marketplace.controller;

import com.imanzi.marketplace.service.JwtService;
import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.Review;
import com.imanzi.marketplace.model.User;
import com.imanzi.marketplace.service.ProductService;
import com.imanzi.marketplace.service.ReviewService;
import com.imanzi.marketplace.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/marketplace/reviews")
@Tag(name = "Review Management", description = "Operations related to product review")
public class ReviewController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final ProductService productService;

    @Autowired
    public ReviewController(JwtService jwtService, UserService userService, ReviewService reviewService, ProductService productService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.productService = productService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public ResponseEntity<Review> createReview(@RequestBody Review review, @RequestParam Long productId, HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = jwtService.getUserFromToken(request);
        User user = userService.getUserFromUserDetails(userDetails);
        Product product = productService.getProductById(productId);

        Review createdReview = reviewService.createReview(review, user, product);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_BUYER')")
    public Review updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        List<Review> reviews = reviewService.getAllReviewsForProduct(product);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}

package com.imanzi.marketplace.service;

import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.Review;
import com.imanzi.marketplace.model.User;
import com.imanzi.marketplace.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {


    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(Review review, User user, Product product) {
        review.setUser(user);
        review.setProduct(product);
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviewsForProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public Review updateReview(Long id, Review review) {
        Review existingReview = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        existingReview.setReviewText(review.getReviewText());
//        existingReview.setRating(review.getRating());
        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}


package com.imanzi.marketplace.repository;

import com.imanzi.marketplace.model.Product;
import com.imanzi.marketplace.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}

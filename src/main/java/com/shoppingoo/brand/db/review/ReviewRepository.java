package com.shoppingoo.brand.db.review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductCode(Integer productCode);
    void deleteById(Integer reviewId);
}
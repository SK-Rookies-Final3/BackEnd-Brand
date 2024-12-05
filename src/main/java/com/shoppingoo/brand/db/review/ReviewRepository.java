package com.shoppingoo.brand.db.review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductCode(int productCode);
    List<Review> findByUserId(int userId);
    void deleteById(Integer reviewCode);
}
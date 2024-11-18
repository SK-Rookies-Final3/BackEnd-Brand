package com.shoppingoo.brand.db.review;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id") // 테이블의 기본 키 필드명에 맞춤
    private int reviewId;

    @Column(name = "order_item_id", nullable = false)
    private int orderItemId;

    @Column(name = "product_code")
    private int productCode;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "weight", nullable = false)
    private int weight;

    @Column(name = "option", length = 50)
    private String option;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Column(name = "star_rating")
    private int starRating;

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    private ReviewRequest reviewRequest;



    // Getter and Setter methods
    public Integer getProductCode() {
        return productCode;
    }

    public void setProductCode(Integer productCode) {
        this.productCode = productCode;
    }

    public int getUserId() { return userId;}

    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public void setStarRating(Integer starRating) {
        this.starRating = starRating;
    }
}
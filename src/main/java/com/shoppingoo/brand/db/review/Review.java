package com.shoppingoo.brand.db.review;
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
    @Column(name = "id") // 테이블의 기본 키 필드명에 맞춤
    private Integer id;

    @Column(name = "order_item_id", nullable = false)
    private Integer orderItemId;

    @Column(name = "product_code")
    private Integer productCode;

    @Column(name = "user_nickname", length = 50)
    private String userNickname;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "option", length = 50)
    private String option;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Column(name = "star_rating")
    private Integer starRating;

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @Column(name = "review_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime reviewDate;
}
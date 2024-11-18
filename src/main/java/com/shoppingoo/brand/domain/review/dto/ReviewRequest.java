package com.shoppingoo.brand.domain.review.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private int reviewId;                 // Review의 기본 키 필드
    private int orderItemId;
    private int productCode;
    private int userId;
    private String username;
    private int height;
    private int weight;
    private String option;
    private String content;
    private String size;
    private int starRating;         // null 가능성 있으므로 int 사용
    private String color;
    private String imageUrl;
    private LocalDateTime reviewDate;   // 리뷰 작성 날짜
}

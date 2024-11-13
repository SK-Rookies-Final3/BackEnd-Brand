package com.shoppingoo.brand.domain.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Integer id;                 // Review의 기본 키 필드
    private Integer orderItemId;
    private Integer productCode;
    private String userNickname;
    private Integer height;
    private Integer weight;
    private String option;
    private String content;
    private String size;
    private Integer starRating;         // null 가능성 있으므로 Integer 사용
    private String color;
    private String imageUrl;
    private LocalDateTime reviewDate;   // 리뷰 작성 날짜
}

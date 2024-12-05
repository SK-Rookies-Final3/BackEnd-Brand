package com.shoppingoo.brand.domain.review.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReviewResponse {
    private int code;
    private int productCode;
    private int userId;
    private String username;
    private int height;
    private int weight;
    private String option;
    private String content;
    private String size;
    private int starRating;
    private String color;
    private LocalDateTime reviewDate;
}

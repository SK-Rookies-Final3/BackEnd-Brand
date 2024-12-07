package com.shoppingoo.brand.domain.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReviewResponse {
    private int reviewCode;
    private int productCode;
    private int userId;
    private String username;
    private String height;
    private String weight;
    //    private String option;
    private List<String> imageUrl;
    private String content;
    private int starRating;
    private LocalDateTime reviewDate;
}
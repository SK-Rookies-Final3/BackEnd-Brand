package com.shoppingoo.brand.domain.review.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private int productCode;
    private int userId;
    private String height;
    private String weight;
    private String username;
    //    private String option;
    private String content;
    private int starRating;
}
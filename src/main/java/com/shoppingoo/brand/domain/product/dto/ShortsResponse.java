package com.shoppingoo.brand.domain.product.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShortsResponse {


    private int id;

    private int productCode;

    private String shortsUrl;

    private String thumbnailUrl;
}

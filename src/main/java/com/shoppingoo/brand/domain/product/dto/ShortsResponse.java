package com.shoppingoo.brand.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

package com.shoppingoo.brand.domain.product.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductResponse {

    private int code;

    private int storeId;

    private String name;

    private int price;

    private int stock;

    private String thumbnail;

    private String textInformation;

    private String images;

    private String category;

    private String color;

    private String clothesSize;

    private String shoesSize;
}

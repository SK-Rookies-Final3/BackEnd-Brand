package com.shoppingoo.brand.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;

    private int price;

    private int stock;

    private String thumbnailUrl;

    private String textInformation;

    private String imageInformation;

    private String category;

    private String color;

    private String clothesSize;

    private String shoesSize;



}

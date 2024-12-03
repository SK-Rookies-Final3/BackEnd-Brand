package com.shoppingoo.brand.db.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    아우터("아우터"),
    상의("상의"),
    하의("하의"),
    치마("치마"),
    신발("신발"),
    모자("모자"),
    가방("가방"),
    악세사리("악세사리")
    ;

    private String description;
}

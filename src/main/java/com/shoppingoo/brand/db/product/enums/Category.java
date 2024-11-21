package com.shoppingoo.brand.db.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    TOP("상의"),
    BOTTOM("하의"),
    PANTS("바지"),
    SKIRT("치마")
    ;

    private String description;
}
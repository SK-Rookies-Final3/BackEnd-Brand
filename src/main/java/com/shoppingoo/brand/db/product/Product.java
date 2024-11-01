package com.shoppingoo.brand.db.product;

import com.shoppingoo.brand.db.product.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    private int code;

    @Column(name = "store_id", nullable = false)
    private int storeId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "stock")
    private int stock;

    @Column(name = "thumbnail_url", length = 300, nullable = false)
    private String thumbnailUrl;

    @Column(name = "text_information", columnDefinition = "TEXT")
    private String textInformation;

    @Column(name = "image_information")
    private String imageInformation;

    @Column(name = "category", length = 50, nullable = false)
    private Category category;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "clothes_size", length = 20)
    private String clothesSize;

    @Column(name = "shoes_size", length = 20)
    private String shoesSize;


}


package com.shoppingoo.brand.db.product;

import com.shoppingoo.brand.db.product.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "store_id", nullable = false)
    private int storeId;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "stock")
    private int stock;

    @ElementCollection
    @CollectionTable(name = "product_thumbnail", joinColumns = @JoinColumn(name = "product_code"))
    @Column(name = "thumbnail", length = 300)
    private List<String> thumbnail;  // S3의 정적 경로 저장

    @Column(name = "text_information", columnDefinition = "TEXT")
    private String textInformation;

    // 상세정보에 들어가는 이미지
    @ElementCollection
    @CollectionTable(name = "product_image", joinColumns = @JoinColumn(name = "product_code"))
    @Column(name = "imageInformation", length = 300)
    private List<String> imageInformation;  // S3의 정적 경로 저장

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category", length = 50, nullable = false)
    private Category category;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "clothes_size", length = 20)
    private String clothesSize;

    @Column(name = "shoes_size", length = 20)
    private String shoesSize;

    @Column(name = "register_at", nullable = false)
    private LocalDateTime registerAt;
}

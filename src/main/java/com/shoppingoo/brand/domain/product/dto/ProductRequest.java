package com.shoppingoo.brand.domain.product.dto;

import com.shoppingoo.brand.db.product.enums.Category;
import org.springframework.web.multipart.MultipartFile;
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

    // 썸네일 이미지 파일들을 MultipartFile[] 배열로 변경
    //private List<MultipartFile> thumbnail;

    private String textInformation;

    // 추가 이미지 파일들을 MultipartFile[] 배열로 변경
    //private List<MultipartFile> images;

    private Category category;

    private String color;

    private String clothesSize;

    private String shoesSize;
}

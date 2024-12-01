package com.shoppingoo.brand.domain.product.converter;

import com.shoppingoo.brand.db.product.enums.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        try {
            int ordinal = Integer.parseInt(source);
            return Category.values()[ordinal]; // EnumType.ORDINAL 기준으로 변환
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid Category value: " + source);
        }
    }
}

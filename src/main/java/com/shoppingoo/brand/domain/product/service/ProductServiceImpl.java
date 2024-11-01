package com.shoppingoo.brand.domain.product.service;

import com.shoppingoo.brand.db.product.Product;
import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.db.product.enums.Category;
import com.shoppingoo.brand.db.store.Store;
import com.shoppingoo.brand.db.store.StoreRepository;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;

    // 초기화
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    // 상품 등록
    public ProductResponse productRegister(int storeId, String thumbnailUrl, ProductRequest productRequest) {
        Product product = modelMapper.map(productRequest, Product.class);

        // 추가 필드 설정
        product.setStoreId(storeId);

        Product savedProduct = productRepository.save(product);
        return new ProductResponse();
    }

    // 상품 수정
    public ProductResponse productUpdate(int storeId, int productCode, String thumbnailUrl, ProductRequest productRequest) {
        Product product = productRepository.findById(productCode).orElse(null);

        if (product == null) {
            throw new RuntimeException("Product not found with code: " + productCode);
        }

        // 수정된 데이터로 필드 업데이트
        modelMapper.map(productRequest, product); // productRequest의 데이터를 product에 매핑
        product.setStoreId(storeId);
        product.setThumbnailUrl(thumbnailUrl); // 썸네일 URL 업데이트

        Product savedProduct = productRepository.save(product);

        // ProductResponse 객체 반환
        return modelMapper.map(savedProduct, ProductResponse.class);
    }


    // 전체 상품 조회
    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    // 단일 상품 조회
    @Override
    public ProductResponse getProductByCode(int productCode) {
        Product product = productRepository.findById(productCode).orElse(null);

        if (product == null) {
            throw new RuntimeException("Product not found with code: " + productCode);
        }
        return modelMapper.map(product, ProductResponse.class);
    }

    // 카테고리 내 전체 상품 조회
    @Override
    public List<ProductResponse> getProductByCategory(Category category) {
        List<Product> products = productRepository.findByCategory(category);

        if (products.isEmpty()) {
            throw new RuntimeException("No products found for category: " + category);
        }

        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());

    }

    @Override
    public void productDelete(int storeId, int productCode) {

        Product product = productRepository.findById(productCode).orElseThrow(() ->
                new RuntimeException("Product not found with code: " + productCode));

        if (product.getStoreId() != storeId) {
            throw new RuntimeException("Store ID mismatch for product: " + productCode);
        }

        productRepository.delete(product);

    }
}

package com.shoppingoo.brand.domain.product.service;

import com.shoppingoo.brand.db.product.Product;
import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.db.store.Store;
import com.shoppingoo.brand.db.store.StoreRepository;
import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;

    // 초기화
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, StoreRepository storeRepository, RestTemplate restTemplate, ModelMapper modelMapper, FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.fileStorageService = fileStorageService;
    }

    // 상품 등록

    public ProductResponse productRegister(int storeId, int userId, ProductRequest productRequest, List<MultipartFile> thumbnail, List<MultipartFile> images) {
        try {
            // 이미지 저장
            String thumbnailUrl = fileStorageService.saveImageFile(thumbnail.get(0)); // 첫 번째 썸네일 파일 저장
            String imageUrl = fileStorageService.saveImageFile(images.get(0)); // 첫 번째 이미지 파일 저장

            // 저장된 URL을 Product 객체에 설정
            Product product = modelMapper.map(productRequest, Product.class);
            product.setThumbnailUrl(thumbnailUrl);
            product.setImageInformation(imageUrl);

            // Product 저장
            productRepository.save(product);

            return modelMapper.map(product, ProductResponse.class);
        } catch (IOException e) {
            // 예외 처리 (로그를 남기거나 적절한 메시지를 반환)
            throw new RuntimeException("Image file upload failed", e);
        }
    }



    // 상품 수정
    public ProductResponse productUpdate(int storeId, int userId, int productCode, ProductRequest productRequest) {

        // storeId에 해당하는 상품을 찾기
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            throw new RuntimeException("Store not found with id " + storeId);
        }

        Store store = storeOptional.get();

        // 해당 storeId가 userId에 속한 가게인지 확인
        if (store.getUserId() != userId) {
            throw new RuntimeException("User does not have permission to access this store");
        }

        Product product = productRepository.findById(productCode).orElse(null);

        if (product == null) {
            throw new RuntimeException("Product not found with code: " + productCode);
        }

        // 수정된 데이터로 필드 업데이트
        modelMapper.map(productRequest, product); // productRequest의 데이터를 product에 매핑
        product.setStoreId(storeId);

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
    public List<ProductResponse> getProductByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);

        if (products.isEmpty()) {
            throw new RuntimeException("No products found for category: " + category);
        }

        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());

    }

    // 상품 삭제
    @Override
    public void productDelete(int storeId, int userId, int productCode) {

        // storeId에 해당하는 상품을 찾기
        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (storeOptional.isEmpty()) {
            throw new RuntimeException("Store not found with id " + storeId);
        }

        Store store = storeOptional.get();

        // 해당 storeId가 userId에 속한 가게인지 확인
        if (store.getUserId() != userId) {
            throw new RuntimeException("User does not have permission to access this store");
        }

        Product product = productRepository.findById(productCode).orElseThrow(() ->
                new RuntimeException("Product not found with code: " + productCode));

        if (product.getStoreId() != storeId) {
            throw new RuntimeException("Store ID mismatch for product: " + productCode);
        }

        productRepository.delete(product);

    }
}

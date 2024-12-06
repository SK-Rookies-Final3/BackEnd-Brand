package com.shoppingoo.brand.domain.product.service;

import com.shoppingoo.brand.db.shorts.Shorts;
import com.shoppingoo.brand.db.product.Product;
import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.db.product.enums.Category;
import com.shoppingoo.brand.db.shorts.ShortsRepository;
import com.shoppingoo.brand.db.store.Store;
import com.shoppingoo.brand.db.store.StoreRepository;
import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.product.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ShortsRepository shortsRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;

    // 초기화
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, StoreRepository storeRepository,
                              ShortsRepository shortsRepository, RestTemplate restTemplate,
                              ModelMapper modelMapper, FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.shortsRepository = shortsRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.fileStorageService = fileStorageService;
    }

    // 상품 등록
    @Override
    public Mono<ProductResponse> productRegister(int storeId, int userId, ProductRequest productRequest,
                                                 List<String> thumbnailFileNames, List<String> imageFileNames) {
        try {
            // Product 객체 생성 및 설정
            Product product = modelMapper.map(productRequest, Product.class);

            // 유저 아이디와 스토어 아이디 설정
            product.setStoreId(storeId);
            product.setUserId(userId);
            product.setRegisterAt(LocalDateTime.now());

            // 썸네일과 이미지 파일 이름을 List<String> 형태로 설정
            product.setThumbnail(thumbnailFileNames);  // 썸네일 파일 이름들을 List로 설정
            product.setImages(imageFileNames); // 여러 이미지 파일 이름들을 List로 설정

            // Product 저장
            productRepository.save(product);

            // 모델 매핑 후 Mono로 감싸서 리턴
            return Mono.just(modelMapper.map(product, ProductResponse.class));  // Mono로 감싸서 반환
        } catch (Exception e) {
            throw new RuntimeException("Product registration failed", e);
        }
    }


    // 가게 별 상품 상세 조회
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductByStoreId(int storeId) {
        List<Product> products = productRepository.findByStoreId(storeId);

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .code(product.getCode())
                        .storeId(product.getStoreId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail().isEmpty() ? null : product.getThumbnail().get(0))  // 첫 번째 썸네일 경로, 비어 있으면 null
                        .category(product.getCategory())
                        .stock(product.getStock())
                        .registerAt(product.getRegisterAt())
                        .build())
                .collect(Collectors.toList());
    }


    // 상품 수정
    @Override
    public Mono<ProductResponse> productUpdate(int storeId, int userId, int productCode, ProductRequest productRequest,
                                               List<String> thumbnailFileNames, List<String> imageFileNames) {
        // storeId에 해당하는 상품을 찾기
        return Mono.fromCallable(() -> storeRepository.findById(storeId))
                .flatMap(storeOptional -> {
                    if (storeOptional.isEmpty()) {
                        return Mono.error(new RuntimeException("Store not found with id " + storeId));
                    }

                    Store store = storeOptional.get();

                    // 해당 storeId가 userId에 속한 가게인지 확인
                    if (store.getUserId() != userId) {
                        return Mono.error(new RuntimeException("User does not have permission to access this store"));
                    }

                    // 상품 조회
                    Product product = productRepository.findById(productCode).orElse(null);
                    if (product == null) {
                        return Mono.error(new RuntimeException("Product not found with code: " + productCode));
                    }

                    // 수정된 데이터로 필드 업데이트
                    modelMapper.map(productRequest, product); // productRequest의 데이터를 product에 매핑
                    product.setStoreId(storeId);

                    // 썸네일과 이미지 파일 이름들을 List로 설정
                    product.setThumbnail(thumbnailFileNames);
                    product.setImages(imageFileNames);

                    // Product 저장
                    return Mono.fromCallable(() -> productRepository.save(product))
                            .map(savedProduct -> modelMapper.map(savedProduct, ProductResponse.class)); // 저장된 Product를 Mono로 감싸서 반환
                });
    }



    // 전체 상품 조회
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .code(product.getCode())
                        .storeId(product.getStoreId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail().isEmpty() ? null : product.getThumbnail().get(0)) // 첫 번째 썸네일 경로, 비어 있으면 null
                        .category(product.getCategory())
                        .stock(product.getStock())
                        .registerAt(product.getRegisterAt())
                        .build())
                .collect(Collectors.toList());
    }


    // 상품 상세 조회
    @Override
    @Transactional(readOnly = true)
    public ProductAllResponse getProductByCode(int productCode) {
        Product product = productRepository.findById(productCode).orElse(null);

        if (product == null) {
            throw new RuntimeException("Product not found with code: " + productCode);
        }

        // 컬렉션을 초기화
        product.getThumbnail().size();
        product.getImages().size();


        return ProductAllResponse.builder()
                .userId(product.getUserId())
                .code(product.getCode())
                .storeId(product.getStoreId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .images(product.getImages())
                .category(product.getCategory())
                .color(product.getColor())
                .clothesSize(product.getClothesSize())
                .shoesSize(product.getShoesSize())
                .registerAt(product.getRegisterAt())
                .stock(product.getStock())
                .textInformation(product.getTextInformation())
                .build();
    }




    // 카테고리 내 전체 상품 조회
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductByCategory(Category category) {
        List<Product> products = productRepository.findByCategory(category);

        // 상품이 없으면 예외 발생
        if (products.isEmpty()) {
            throw new RuntimeException("No products found for category: " + category);
        }

        // 필요한 필드만 설정
        return products.stream()
                .map(product -> ProductResponse.builder()
                        .code(product.getCode())
                        .storeId(product.getStoreId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail().isEmpty() ? null : product.getThumbnail().get(0))
                        .category(product.getCategory())
                        .stock(product.getStock())
                        .registerAt(product.getRegisterAt())
                        .build())
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

    // 사용자(owner)의 본인 가게의 상품 전체 조회
    // 모델매퍼 지연 오류로 각각 response하는 방법 사용
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductByUserId(int userId) {
        List<Product> products = productRepository.findByUserId(userId);

        // 상품이 없으면 빈 리스트 반환
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        // 필요한 필드만 설정
        return products.stream()
                .map(product -> ProductResponse.builder()
                        .code(product.getCode())
                        .storeId(product.getStoreId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail().isEmpty() ? null : product.getThumbnail().get(0)) // 첫 번째 썸네일 경로만 반환
                        .category(product.getCategory())
                        .stock(product.getStock())
                        .registerAt(product.getRegisterAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 사용자(owner)의 본인 가게의 상품 전체 조회 -- order 사용
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductByUserIds(int userId) {
        List<Product> products = productRepository.findByUserId(userId);

        // 상품이 없으면 빈 리스트 반환
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        // 필요한 필드만 설정
        return products.stream()
                .map(product -> ProductResponse.builder()
                        .code(product.getCode())
                        .storeId(product.getStoreId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail().isEmpty() ? null : product.getThumbnail().get(0)) // 첫 번째 썸네일 경로만 반환
                        .category(product.getCategory())
                        .stock(product.getStock())
                        .registerAt(product.getRegisterAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 상품 별 재고 조회
    @Override
    @Transactional(readOnly = true)
    public int getProductStock(int productCode) {
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found with code: " + productCode));

        return product.getStock();
    }

    // 상품 별 재고 수정
    @Override
    public ProductResponse updateProductStock(int productCode, StockRequest stockRequest) {
        // 상품 조회
        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found with code " + productCode));

        // 재고 업데이트
        product.setStock(stockRequest.getStock());
        Product updatedProduct = productRepository.save(product);

        // 업데이트된 상품 정보를 ProductResponse로 매핑
        // thumbnail은 지연로딩을 일으키기 때문에 제외
        return ProductResponse.builder()
                .code(updatedProduct.getCode())
                .storeId(updatedProduct.getStoreId())
                .name(updatedProduct.getName())
                .price(updatedProduct.getPrice())
                .category(updatedProduct.getCategory())
                .stock(updatedProduct.getStock())
                .registerAt(updatedProduct.getRegisterAt())
                .build();
    }

    // 상품 별 숏츠 조회
    @Override
    public List<ShortsResponse> getShortsByCode(int productCode){
        List<Shorts> shortsList = shortsRepository.findByProductCode(productCode);

        if (shortsList.isEmpty()) {
            return Collections.emptyList();
        }

        return shortsList.stream()
                .map(shorts -> ShortsResponse.builder()
                        .id(shorts.getId())
                        .productCode(shorts.getProductCode())
                        .shortsUrl(shorts.getShortsUrl())
                        .thumbnailUrl(shorts.getThumbnailUrl())
                        .build())
                .collect(Collectors.toList());

    }




}

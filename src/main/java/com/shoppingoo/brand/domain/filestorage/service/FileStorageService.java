package com.shoppingoo.brand.domain.filestorage.service;

import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

public interface FileStorageService {
    Mono<String> saveImageFile(Part part);  // Part로 수정
}

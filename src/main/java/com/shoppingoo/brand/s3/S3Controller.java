package com.shoppingoo.brand.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class S3Controller {
    private final S3Client s3Client;

    @GetMapping("/presigned-url/{filename}")
    public Mono<String> getPresignedUrl(@PathVariable String filename) {
        return s3Client.generatePreSignedUrl(filename);  // 프리사이드 URL 생성
    }
}

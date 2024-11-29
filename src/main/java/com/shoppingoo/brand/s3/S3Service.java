package com.shoppingoo.brand.s3;

import com.shoppingoo.brand.config.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final WebClient.Builder webClientBuilder;
    private final AwsS3Properties awsS3Properties;

    // S3 버킷 URL
    private  String S3_BUCKET_URL = awsS3Properties.getBucketUrl();

    // 단일 파일 업로드 및 프리사이드 URL 생성
    public Mono<String> uploadFile(MultipartFile file, String folderName) {
        String fileName = folderName + "/" + file.getOriginalFilename();

        return WebClient.create(S3_BUCKET_URL)
                .post()
                .uri("/upload") // 업로드 엔드포인트 (적절히 수정)
                .bodyValue(file)
                .retrieve()
                .bodyToMono(String.class) // 업로드 결과로 받은 파일 URL
                .map(fileUrl -> fileUrl);  // S3 URL 반환
    }

    // 다수의 파일 업로드 처리 및 프리사이드 URL 생성
    public Mono<List<String>> uploadFiles(List<MultipartFile> files, String folderName) {
        List<Mono<String>> fileUploads = files.stream()
                .map(file -> uploadFile(file, folderName))
                .collect(Collectors.toList());

        return Mono.zip(fileUploads, objects -> {
            return List.of(objects).stream().map(o -> (String) o).collect(Collectors.toList());
        });
    }

    public Mono<String> generatePreSignedUrl(String filePath) {
        return WebClient.create(S3_BUCKET_URL)
                .get()
                .uri(uriBuilder -> uriBuilder.path("/presigned-url/{filePath}").build(filePath))
                .retrieve()
                .bodyToMono(String.class); // 프리사이드 URL 반환
    }
}

package com.shoppingoo.brand.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.shoppingoo.brand.config.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Client {

    private final AwsS3Properties awsS3Properties;

    // S3 버킷 URL (실제 버킷 이름을 사용)
    private String S3_BUCKET_NAME = awsS3Properties.getBucketName(); // 실제 S3 버킷 이름

    // AmazonS3 클라이언트 생성
    private AmazonS3 createS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3Properties.getAccess(), awsS3Properties.getSecret());

        return AmazonS3ClientBuilder.standard()
                .withRegion("")  // S3 지역 설정
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    // 비동기적으로 프리사이드 URL 생성
    public Mono<String> generatePreSignedUrl(String filePath) {
        // 비동기적으로 AWS SDK 사용 (Mono.fromCallable 사용)
        return Mono.fromCallable(() -> {
            try {
                AmazonS3 amazonS3 = createS3Client(); // 클라이언트 생성
                // 파일 경로와 버킷 이름을 바탕으로 프리사이드 URL을 생성
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(S3_BUCKET_NAME, filePath)
                                .withMethod(com.amazonaws.HttpMethod.GET)  // GET 방식으로 URL을 생성
                                .withExpiration(new Date(System.currentTimeMillis() + 3600000)); // 1시간 동안 유효

                // 프리사이드 URL 생성
                URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
                return url.toString(); // URL을 문자열로 반환
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate presigned URL", e);
            }
        });
    }
}

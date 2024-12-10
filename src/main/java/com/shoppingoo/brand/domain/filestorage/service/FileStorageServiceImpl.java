// package com.shoppingoo.brand.domain.filestorage.service;

// import com.amazonaws.auth.AWSCredentials;
// import com.amazonaws.auth.AWSStaticCredentialsProvider;
// import com.amazonaws.auth.BasicAWSCredentials;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3ClientBuilder;
// import com.amazonaws.services.s3.model.PutObjectRequest;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.codec.multipart.Part;
// import org.springframework.stereotype.Service;
// import reactor.core.publisher.Mono;
// import reactor.core.scheduler.Schedulers;
// import com.amazonaws.services.s3.model.ObjectMetadata;
// import com.amazonaws.SdkClientException;
// import com.amazonaws.AmazonServiceException;
// import org.springframework.core.io.buffer.DataBuffer;
// import org.springframework.core.io.buffer.DataBufferUtils;

// import java.io.ByteArrayInputStream;

// @Service
// public class FileStorageServiceImpl implements FileStorageService {

//     @Value("${aws.access.key.id}")
//     private String awsAccessKeyId;

//     @Value("${aws.secret.access.key}")
//     private String awsSecretAccessKey;

//     @Value("${aws.region}")
//     private String awsRegion;

//     @Value("${aws.s3.bucket.name}")
//     private String bucketName;

//     @Override
//     public Mono<String> saveImageFile(Part part) {
//         // AWS 자격 증명 설정
//         AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

//         // S3 클라이언트 초기화
//         AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                 .withRegion(awsRegion)
//                 .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                 .build();

//         // 파일 이름 추출
//         String fileName = part.headers().getFirst("Content-Disposition")
//                 .substring(part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10,
//                         part.headers().getFirst("Content-Disposition").indexOf("\"",
//                                 part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10));

//         // 비동기 방식으로 파일 저장
//         return part.content()
//                 .reduce((dataBuffer1, dataBuffer2) -> {
//                     // 여러 청크를 하나의 DataBuffer로 합침
//                     dataBuffer1.write(dataBuffer2.asByteBuffer());
//                     DataBufferUtils.release(dataBuffer2); // 메모리 해제
//                     return dataBuffer1;
//                 })
//                 .flatMap(dataBuffer -> Mono.fromCallable(() -> {
//                     try {
//                         // S3에 업로드할 파일 내용 준비
//                         byte[] fileContent = new byte[dataBuffer.readableByteCount()];
//                         dataBuffer.read(fileContent); // DataBuffer 내용을 읽음
//                         DataBufferUtils.release(dataBuffer); // 메모리 해제

//                         // S3에 파일 업로드
//                         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent);
//                         ObjectMetadata metadata = new ObjectMetadata();
//                         metadata.setContentLength(fileContent.length);

//                         // S3에 업로드
//                         s3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata));

//                         // 업로드된 파일 URL 반환
//                         return s3Client.getUrl(bucketName, fileName).toString();
//                     } catch (Exception e) {
//                         throw new RuntimeException("파일 업로드 중 오류 발생", e);
//                     }
//                 }))
//                 .then(Mono.just(s3Client.getUrl(bucketName, fileName).toString()));
//     }

// }


package com.shoppingoo.brand.domain.filestorage.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.SdkClientException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;

import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${aws.access.key.id}")
    private String awsAccessKeyId;

    @Value("${aws.secret.access.key}")
    private String awsSecretAccessKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public Mono<String> saveImageFile(Part part) {
        // AWS 자격 증명 설정
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

        // S3 클라이언트 초기화
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        // 파일 이름 추출
        String fileName = part.headers().getFirst("Content-Disposition")
                .substring(part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10,
                        part.headers().getFirst("Content-Disposition").indexOf("\"",
                                part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10));

        // 비동기 방식으로 파일 저장
        return part.content()
                .reduce((dataBuffer1, dataBuffer2) -> {
                    // 여러 청크를 하나의 DataBuffer로 합침
                    dataBuffer1.write(dataBuffer2.asByteBuffer());
                    DataBufferUtils.release(dataBuffer2); // 메모리 해제
                    return dataBuffer1;
                })
                .flatMap(dataBuffer -> Mono.fromCallable(() -> {
                    try {
                        // 원본 이미지 데이터 읽기
                        byte[] fileContent = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(fileContent);
                        DataBufferUtils.release(dataBuffer); // 메모리 해제

                        // 이미지 처리: 9:16 비율로 크롭 및 리사이즈
                        byte[] processedImage = processImageTo9x16(fileContent);

                        // S3에 업로드할 파일 준비
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(processedImage);
                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(processedImage.length);
                        metadata.setContentType("image/jpeg");

                        // S3 업로드
                        s3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata));

                        // 업로드된 파일 URL 반환
                        return s3Client.getUrl(bucketName, fileName).toString();
                    } catch (SdkClientException e) {
                        throw new RuntimeException("AWS S3 업로드 중 오류 발생", e);
                    } catch (Exception e) {
                        throw new RuntimeException("이미지 처리 중 오류 발생", e);
                    }
                }))
                .then(Mono.just(s3Client.getUrl(bucketName, fileName).toString()));
    }

    // 이미지 처리 로직 (9:16 비율로 변환)
    private byte[] processImageTo9x16(byte[] fileContent) throws Exception {
        // 이미지 로드
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
        BufferedImage originalImage = ImageIO.read(inputStream);

        // 원본 이미지 크기
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 9:16 비율 계산
        int targetHeight = originalHeight;
        int targetWidth = (originalHeight * 9) / 16;

        // 너비가 부족한 경우, 높이 기준으로 크기 재조정
        if (targetWidth > originalWidth) {
            targetWidth = originalWidth;
            targetHeight = (originalWidth * 16) / 9;
        }

        // 중심 크롭
        int cropX = (originalWidth - targetWidth) / 2;
        int cropY = (originalHeight - targetHeight) / 2;
        BufferedImage croppedImage = originalImage.getSubimage(cropX, cropY, targetWidth, targetHeight);

        // 크기 조정 (선택 사항)
        BufferedImage resizedImage = new BufferedImage(1080, 1920, BufferedImage.TYPE_INT_RGB); // 1080x1920 해상도로 설정
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(croppedImage, 0, 0, 1080, 1920, null);
        g.dispose();

        // 처리된 이미지를 바이트 배열로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);
        return outputStream.toByteArray();
    }
}

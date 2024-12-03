// package com.shoppingoo.brand.domain.filestorage.service;

// import org.springframework.http.codec.multipart.Part;
// import org.springframework.stereotype.Service;
// import reactor.core.publisher.Mono;
// import reactor.core.scheduler.Schedulers;

// import java.io.File;
// import java.io.IOException;
// import java.io.OutputStream;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.StandardOpenOption;

// @Service
// public class FileStorageServiceImpl implements FileStorageService {

//     @Override
//     public Mono<String> saveImageFile(Part part) {
//         // 파일 이름 추출
//         String fileName = part.headers().getFirst("Content-Disposition")
//                 .substring(part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10,
//                         part.headers().getFirst("Content-Disposition").indexOf("\"",
//                                 part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10));

//         // 상대 경로로 파일 저장 디렉토리 설정 (웹 서버 기준)
//         String relativeUploadDir = "/uploads";
//         String uploadDir = System.getProperty("user.dir") + File.separator + relativeUploadDir;

//         // 디렉토리가 없다면 생성
//         File directory = new File(uploadDir);
//         if (!directory.exists() && !directory.mkdirs()) {
//             throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + uploadDir);
//         }

//         // 파일 경로 설정
//         Path path = Path.of(uploadDir, fileName);

//         // 비동기 방식으로 파일 저장
//         return part.content()
//                 .publishOn(Schedulers.boundedElastic()) // 블로킹 작업을 별도 스레드에서 실행
//                 .flatMap(dataBuffer -> Mono.fromCallable(() -> {
//                     try {
//                         // DataBuffer의 내용을 파일로 저장
//                         Files.write(path, dataBuffer.asByteBuffer().array(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//                     } catch (IOException e) {
//                         throw new RuntimeException("파일 저장 중 오류 발생", e);
//                     }
//                     return path.toString();
//                 }))
//                 .then(Mono.just(relativeUploadDir + File.separator + fileName)); // 상대 경로 반환
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
import com.amazonaws.AmazonServiceException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;

import java.io.ByteArrayInputStream;

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
                        // S3에 업로드할 파일 내용 준비
                        byte[] fileContent = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(fileContent); // DataBuffer 내용을 읽음
                        DataBufferUtils.release(dataBuffer); // 메모리 해제

                        // S3에 파일 업로드
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileContent);
                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(fileContent.length);

                        // S3에 업로드
                        s3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, metadata));

                        // 업로드된 파일 URL 반환
                        return s3Client.getUrl(bucketName, fileName).toString();
                    } catch (Exception e) {
                        throw new RuntimeException("파일 업로드 중 오류 발생", e);
                    }
                }));
    }
}

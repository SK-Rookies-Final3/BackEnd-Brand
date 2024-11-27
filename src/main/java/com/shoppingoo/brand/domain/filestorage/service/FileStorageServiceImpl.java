package com.shoppingoo.brand.domain.filestorage.service;

import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public Mono<String> saveImageFile(Part part) {
        // 파일 이름 추출
        String fileName = part.headers().getFirst("Content-Disposition")
                .substring(part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10,
                        part.headers().getFirst("Content-Disposition").indexOf("\"",
                                part.headers().getFirst("Content-Disposition").indexOf("filename=\"") + 10));

        // 상대 경로로 파일 저장 디렉토리 설정 (웹 서버 기준)
        String relativeUploadDir = "uploads";
        String uploadDir = System.getProperty("user.dir") + File.separator + relativeUploadDir;

        // 디렉토리가 없다면 생성
        File directory = new File(uploadDir);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("디렉토리를 생성할 수 없습니다: " + uploadDir);
        }

        // 파일 경로 설정
        Path path = Path.of(uploadDir, fileName);

        // 비동기 방식으로 파일 저장
        return part.content()
                .publishOn(Schedulers.boundedElastic()) // 블로킹 작업을 별도 스레드에서 실행
                .flatMap(dataBuffer -> Mono.fromCallable(() -> {
                    try (OutputStream outputStream = Files.newOutputStream(path)) {
                        outputStream.write(dataBuffer.asByteBuffer().array());
                        // 상대 경로 반환
                        return relativeUploadDir + File.separator + fileName;
                    } catch (IOException e) {
                        throw new RuntimeException("파일 저장 중 오류 발생", e);
                    }
                }))
                .last(); // 마지막 저장된 파일 상대 경로 반환
    }
}

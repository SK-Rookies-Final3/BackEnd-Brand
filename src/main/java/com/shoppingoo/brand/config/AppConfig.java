package com.shoppingoo.brand.config;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    //swagger
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shoppingoo Brand API")
                        .version("1.0")
                        .description("API documentation for Shoppingoo Brand Review Service"));
    }

    @Bean
    public AmazonS3 amazonS3() {
        // AWS 자격 증명 설정
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("${aws.s3.access}", "${aws.s3.secret}");

        // AmazonS3 객체 생성 및 반환
        return AmazonS3ClientBuilder.standard()
                .withRegion("ap-northeast-2") // 사용할 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}

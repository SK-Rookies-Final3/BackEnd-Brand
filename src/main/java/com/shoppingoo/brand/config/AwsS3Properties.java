package com.shoppingoo.brand.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
public class AwsS3Properties {

    private String access;
    private String secret;
    private String bucketName;
    private String region;
    private String bucketUrl;

}

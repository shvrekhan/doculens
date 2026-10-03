package com.dev.doculens.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class StorageConfig {

    @Bean
    public S3Client s3Client(
            @Value("${rustfs.endpoint}") String endpoint,
            @Value("${rustfs.access-key}") String accessKey,
            @Value("${rustfs.secret-key}") String secretKey,
            @Value("${rustfs.region}") String region) {

        AwsBasicCredentials credentials =
                AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(credentials)
                )
                .region(Region.of(region))
                .forcePathStyle(true)
                .build();
    }
}
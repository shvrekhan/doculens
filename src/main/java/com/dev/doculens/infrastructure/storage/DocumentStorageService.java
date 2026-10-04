package com.dev.doculens.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class DocumentStorageService {

    private final S3Client s3Client;

    @Value("${rustfs.bucket}")
    private String bucket;

    public DocumentStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(String key, byte[] content, String contentType) {
        PutObjectRequest.Builder requestBuilder = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key);

        if (contentType != null && !contentType.isBlank()) {
            requestBuilder.contentType(contentType);
        }

        s3Client.putObject(
                requestBuilder.build(),
                RequestBody.fromBytes(content)
        );
    }
}

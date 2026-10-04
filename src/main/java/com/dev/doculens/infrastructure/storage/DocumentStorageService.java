package com.dev.doculens.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class DocumentStorageService {

    private final S3Client s3Client;

    @Value("${rustfs.bucket}")
    private String bucket;

    public DocumentStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(String fileName, byte[] content) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(content)
        );
    }

    public String upload(MultipartFile invoice) {

        String fileName = "invoices/"+ UUID.randomUUID() +"/"+invoice.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(invoice.getContentType())
                .build();

        try {
            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            invoice.getInputStream(),
                            invoice.getSize()
                    )
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload invoice", e);
        }
    }
}

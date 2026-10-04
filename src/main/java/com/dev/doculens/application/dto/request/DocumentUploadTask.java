package com.dev.doculens.application.dto.request;

public record DocumentUploadTask(
        String originalFilename,
        String contentType,
        long size,
        byte[] content
) {}

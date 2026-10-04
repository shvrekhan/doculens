package com.dev.doculens.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DocStatus(
        UUID id,
        String fileName,
        String contentType,
        long fileSize,
        String fileHash,
        String storageKey,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}

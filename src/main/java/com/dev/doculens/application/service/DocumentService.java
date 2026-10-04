package com.dev.doculens.application.service;

import com.dev.doculens.application.dto.request.DocumentUploadTask;
import com.dev.doculens.application.util.ApplicationUtil;
import com.dev.doculens.domain.enums.DocProcessingStatus;
import com.dev.doculens.domain.model.DocStatus;
import com.dev.doculens.infrastructure.persistence.repository.DocStatusRepository;
import com.dev.doculens.infrastructure.storage.DocumentStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentStorageService storageService;
    private final ApplicationUtil applicationUtil;
    private final DocStatusRepository docStatusRepository;

    public DocumentService(DocumentStorageService storageService,
                           ApplicationUtil applicationUtil,
                           DocStatusRepository docStatusRepository) {
        this.storageService = storageService;
        this.applicationUtil = applicationUtil;
        this.docStatusRepository = docStatusRepository;
    }

    @Async("DocUploadThreadPool")
    public void uploadDocumentAsync(DocumentUploadTask task) {
        UUID id = UUID.randomUUID();
        String fileName = task.originalFilename();
        String filePath = "staging/invoices/" + id + "/" + fileName;

        try {
            String fileHash = applicationUtil.calculateSha256(task.content());
            OffsetDateTime now = OffsetDateTime.now();

            DocStatus docStatusObject = new DocStatus(
                    id,
                    fileName,
                    task.contentType(),
                    task.size(),
                    fileHash,
                    filePath,
                    DocProcessingStatus.PENDING.name(),
                    now,
                    now
            );

            docStatusRepository.save(docStatusObject);
            log.info("Saved initial doc_status with id: {} and status: PENDING", id);

            storageService.upload(filePath, task.content(), task.contentType());
            log.info("Uploaded document to storage successfully: {}", filePath);

            docStatusRepository.updateStatus(id, DocProcessingStatus.PROCESSING);
            log.info("Updated doc_status id: {} to PROCESSING", id);

        } catch (Exception e) {
            log.error("Failed async upload for file: {}, marking as FAILED", fileName, e);
            try {
                docStatusRepository.updateStatus(id, DocProcessingStatus.FAILED);
            } catch (Exception dbEx) {
                log.error("Failed to update status to FAILED for id: {}", id, dbEx);
            }
        }
    }
}

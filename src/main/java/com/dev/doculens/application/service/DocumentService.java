package com.dev.doculens.application.service;

import com.dev.doculens.infrastructure.storage.DocumentStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentStorageService storageService;

    public DocumentService(DocumentStorageService storageService) {
        this.storageService = storageService;
    }

    public void uploadDocuments(List<MultipartFile> files) {
        files.forEach(storageService::upload);
    }
}

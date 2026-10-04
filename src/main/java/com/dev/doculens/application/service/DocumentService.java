package com.dev.doculens.application.service;

import com.dev.doculens.infrastructure.storage.DocumentStorageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentStorageService storageService;

    public DocumentService(DocumentStorageService storageService) {
        this.storageService = storageService;
    }

    @Async("DocUploadThreadPool")
    public void uploadDocuments(MultipartFile files) {
        System.out.println(Thread.currentThread().getName());
    }
}

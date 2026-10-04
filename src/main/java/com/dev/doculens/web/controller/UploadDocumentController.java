package com.dev.doculens.web.controller;

import com.dev.doculens.application.dto.response.BaseResponse;
import com.dev.doculens.application.service.DocumentService;
import com.dev.doculens.infrastructure.storage.DocumentStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UploadDocumentController {

    private final DocumentStorageService storageService;
    private final DocumentService documentService;

    public UploadDocumentController(
            DocumentStorageService storageService,
            DocumentService documentService
    ) {
        this.storageService = storageService;
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadInvoice(@RequestParam("invoice") List<MultipartFile> invoice) {
        invoice.forEach(documentService::uploadDocuments);

        return ResponseEntity.ok(new BaseResponse("QUEUED","Invoice Queued Successfully."));
    }
}

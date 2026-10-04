package com.dev.doculens.web.controller;

import com.dev.doculens.application.dto.request.DocumentUploadTask;
import com.dev.doculens.application.dto.response.BaseResponse;
import com.dev.doculens.application.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class UploadDocumentController {

    private final DocumentService documentService;

    public UploadDocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> uploadInvoice(@RequestParam("invoice") List<MultipartFile> invoices) {
        for (MultipartFile file : invoices) {
            try {
                DocumentUploadTask task = new DocumentUploadTask(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getSize(),
                        file.getBytes()
                );
                documentService.uploadDocumentAsync(task);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read uploaded file: " + file.getOriginalFilename(), e);
            }
        }

        return ResponseEntity.ok(new BaseResponse("QUEUED", "Invoices queued successfully for background upload"));
    }
}

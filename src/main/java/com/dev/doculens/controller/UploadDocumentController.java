package com.dev.doculens.controller;

import com.dev.doculens.storage.DocumentStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadDocumentController {

    private final DocumentStorageService storageService;

    public UploadDocumentController(DocumentStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadInvoice(@RequestParam("invoice") MultipartFile invoice) {

        String fileName = storageService.upload(invoice);

        return ResponseEntity.ok(
                "Invoice uploaded successfully: " + fileName
        );
    }
}

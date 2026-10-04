package com.dev.doculens.web.exception;

import com.dev.doculens.application.dto.response.BaseResponse;
import com.dev.doculens.domain.exception.DocumentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<BaseResponse> handleNotFound(DocumentNotFoundException ex) {
        BaseResponse response = new BaseResponse();
        response.status = "error";
        response.message = ex.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse> handleMaxSizeExceeded(MaxUploadSizeExceededException ex) {
        BaseResponse response = new BaseResponse();
        response.status = "error";
        response.message = "File size exceeds the maximum allowed limit";
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGeneric(Exception ex) {
        BaseResponse response = new BaseResponse();
        response.status = "error";
        response.message = "Something went wrong";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

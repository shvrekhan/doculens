package com.dev.doculens.application.dto.response;

public class BaseResponse {
    public String status;
    public String message;

    public BaseResponse() {
    }

    public BaseResponse(String s, String s1) {
        this.status = s;
        this.message = s1;
    }
}

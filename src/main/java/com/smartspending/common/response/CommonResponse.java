package com.smartspending.common.response;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class CommonResponse<T> {

    private final Integer status;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    public CommonResponse(Integer status, String message, T data, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public CommonResponse(Integer status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.timestamp = timestamp;
    }

    public CommonResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
        this.timestamp = null;
    }

    public CommonResponse() {
        this.status = null;
        this.message = null;
        this.data = null;
        this.timestamp = null;
    }
}

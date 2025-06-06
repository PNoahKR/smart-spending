package com.smartspending.common.util;

import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.response.CommonResponse;

import static com.smartspending.common.exception.CommonResponseCode.SUCCESS;

public class ApiResponseUtil {

    public static <T>CommonResponse<T> success() {
        return new CommonResponse<>(SUCCESS.getHttpStatus().value(), SUCCESS.getMessage(), null);
    }

    public static <T>CommonResponse<T> success(T data) {
        return new CommonResponse<>(SUCCESS.getHttpStatus().value(), SUCCESS.getMessage(), data);
    }

    public static <T>CommonResponse<T> failure(CommonResponseCode code) {
        return new CommonResponse<>(code.getHttpStatus().value(), code.getMessage(), null);
    }

    public static <T>CommonResponse<T> failure(Integer statusCode, String message) {
        return new CommonResponse<>(statusCode, message);
    }
}

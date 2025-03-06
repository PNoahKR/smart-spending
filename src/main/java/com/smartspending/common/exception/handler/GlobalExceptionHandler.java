package com.smartspending.common.exception.handler;

import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<CommonResponse<Object>> handleException(Exception e) {
        log.error("Exception", e);
        return toErrrorResponseEntity(CommonResponseCode.SERVER_ERROR);
    }

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<CommonResponse<Object>> handleCustomException(CustomException e) {
        log.warn("CustomException", e);
        return toErrrorResponseEntity(e.getCode());
    }

    protected ResponseEntity<CommonResponse<Object>> toErrrorResponseEntity(CommonResponseCode commonResponseCode) {
        return ResponseEntity
                .status(commonResponseCode.getHttpStatus())
                .body(ApiResponseUtil.failure(commonResponseCode));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("Exception", exception);
        CommonResponse<Object> response = ApiResponseUtil.failure(statusCode.value(), exception.getMessage());
        return ResponseEntity.status(statusCode).body(response);
    }
}

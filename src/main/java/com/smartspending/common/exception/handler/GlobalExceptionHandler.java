package com.smartspending.common.exception.handler;

import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("Exception", ex);
        CommonResponse<Object> response = ApiResponseUtil.failure(statusCode.value(), ex.getMessage());
        return ResponseEntity.status(statusCode).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("ValidationException", ex);

        FieldError fistFieldError = ex.getBindingResult().getFieldErrors().get(0);
        CommonResponse<Object> response = ApiResponseUtil.failure(status.value(), fistFieldError.getField() + " : " + fistFieldError.getDefaultMessage());

        return ResponseEntity.status(status).body(response);
    }


}

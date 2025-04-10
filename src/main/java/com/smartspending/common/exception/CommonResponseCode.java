package com.smartspending.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonResponseCode {

    SUCCESS(HttpStatus.OK, "성공"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디 또는 비밀번호가 일치하지 않습니다"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다"),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "잘못된 권한입니다"),
    NOT_FOUND_JWT_TOKEN(HttpStatus.NOT_FOUND, "토큰 정보가 없습니다"),
    EMAIL_NOT_VERIFIED(HttpStatus.UNAUTHORIZED, "잘못된 인증 코드 입니다"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테코리명 입니다"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리 입니다"),
    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND,"준재하지 않는 내역 입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

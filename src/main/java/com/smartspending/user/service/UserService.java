package com.smartspending.user.service;

import com.smartspending.user.dto.request.*;
import com.smartspending.user.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    boolean duplicateEmail(String email);

    void sendVerificationCode(String email);

    boolean verifyEmail(VerifyCodeRequestDto requestDto);

    Long register(RegisterRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    void resetPassword(ResetPasswordDto requestDto);

    LoginResponseDto reCreateAccessToken(RequestTokenDto requestDto);

    void logout(HttpServletRequest request);
}

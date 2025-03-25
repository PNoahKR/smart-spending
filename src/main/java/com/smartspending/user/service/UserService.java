package com.smartspending.user.service;

import com.smartspending.user.dto.request.CompleteRegisterRequestDto;
import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.EmailVerifyRequestDto;
import com.smartspending.user.dto.request.RequestTokenDto;
import com.smartspending.user.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    void verifyUserEmail(EmailVerifyRequestDto requestDto);

    Long completeUserRegister(CompleteRegisterRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    LoginResponseDto reCreateAccessToken(RequestTokenDto requestDto);

    void logout(HttpServletRequest request);
}

package com.smartspending.user.service;

import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.RegisterRequestDto;
import com.smartspending.user.dto.request.RequestTokenDto;
import com.smartspending.user.dto.response.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    Long registerUser(RegisterRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);

    LoginResponseDto reCreateAccessToken(RequestTokenDto requestDto);

    void logout(HttpServletRequest request);
}

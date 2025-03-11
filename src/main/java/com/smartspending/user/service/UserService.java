package com.smartspending.user.service;

import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.RegisterRequestDto;

public interface UserService {
    Long registerUser(RegisterRequestDto requestDto);

    Long login(LoginRequestDto requestDto);
}

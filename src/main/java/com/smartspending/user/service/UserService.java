package com.smartspending.user.service;

import com.smartspending.user.dto.request.RegisterRequestDto;

public interface UserService {
    Long registerUser(RegisterRequestDto requestDto);
}

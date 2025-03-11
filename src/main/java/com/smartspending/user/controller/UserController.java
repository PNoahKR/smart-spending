package com.smartspending.user.controller;

import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.RegisterRequestDto;
import com.smartspending.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public CommonResponse<Long> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ApiResponseUtil.success(userService.registerUser(registerRequestDto));
    }

    @PostMapping("/login")
    public CommonResponse<Long> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponseUtil.success(userService.login(loginRequestDto));
    }
}

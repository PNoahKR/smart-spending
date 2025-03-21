package com.smartspending.user.controller;

import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.RegisterRequestDto;
import com.smartspending.user.dto.request.RequestTokenDto;
import com.smartspending.user.dto.response.LoginResponseDto;
import com.smartspending.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CommonResponse<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponseUtil.success(userService.login(loginRequestDto));
    }

    @PostMapping("/reissue")
    public CommonResponse<LoginResponseDto> reissue(@RequestBody RequestTokenDto requestTokenDto) {
        return ApiResponseUtil.success(userService.reCreateAccessToken(requestTokenDto));
    }

    @PostMapping("/logout")
    public CommonResponse<Void> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponseUtil.success();
    }

    @GetMapping("/test")
    public String test() {
        return "성공";
    }
}

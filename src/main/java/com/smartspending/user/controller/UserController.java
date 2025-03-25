package com.smartspending.user.controller;

import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.user.dto.request.CompleteRegisterRequestDto;
import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.EmailVerifyRequestDto;
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
    public CommonResponse<Void> register(@RequestBody EmailVerifyRequestDto emailVerifyRequestDto) {
        userService.verifyUserEmail(emailVerifyRequestDto);
        return ApiResponseUtil.success();
    }

    @PostMapping("/register-verified")
    public CommonResponse<Long> registerVerifyEmail(@RequestBody CompleteRegisterRequestDto completeRegisterRequestDto) {
        return ApiResponseUtil.success(userService.CompleteUserRegister(completeRegisterRequestDto));
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

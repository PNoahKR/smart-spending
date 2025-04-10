package com.smartspending.user.controller;

import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.user.dto.request.*;
import com.smartspending.user.dto.response.LoginResponseDto;
import com.smartspending.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/register/check-email")
    public CommonResponse<Boolean> checkEmail(@RequestParam String email) {
        return ApiResponseUtil.success(userService.duplicateEmail(email));
    }

    @PostMapping("/register/send-code")
    public CommonResponse<Void> sendCode(@RequestParam String email) {
        userService.sendVerificationCode(email);
        return ApiResponseUtil.success();
    }

    @PostMapping("/register/verify-code")
    public CommonResponse<Boolean> verifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto) {
        return ApiResponseUtil.success(userService.verifyEmail(verifyCodeRequestDto));
    }

    @PostMapping("/register")
    public CommonResponse<Long> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        return ApiResponseUtil.success(userService.register(registerRequestDto));
    }

    @PostMapping("/login")
    public CommonResponse<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ApiResponseUtil.success(userService.login(loginRequestDto));
    }

    @PostMapping("/findPassword/send-code")
    public CommonResponse<Void> sendPasswordResetCode(@RequestParam String email) {
        userService.sendVerificationCode(email);
        return ApiResponseUtil.success();
    }

    @PostMapping("/findPassword/verify-code")
    public CommonResponse<Boolean> verifyPasswordResetCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto) {
        return ApiResponseUtil.success(userService.verifyEmail(verifyCodeRequestDto));
    }

    @PostMapping("/findPassword")
    public CommonResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto);
        return ApiResponseUtil.success();
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
}

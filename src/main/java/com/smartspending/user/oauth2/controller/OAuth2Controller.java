package com.smartspending.user.oauth2.controller;

import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import com.smartspending.user.dto.response.LoginResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    // 소셜 로그인 성공
    @GetMapping("/social-login")
    public CommonResponse<LoginResponseDto> socialLogin(@RequestHeader("Authorization") String authorizationHeader) {
        String accessToken = authorizationHeader.substring("Bearer ".length());
        String refreshToken = authorizationHeader.substring("Bearer ".length());

        LoginResponseDto loginResponseDto = new LoginResponseDto(accessToken, refreshToken);

        return ApiResponseUtil.success(loginResponseDto);
    }
}

package com.smartspending.common.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.auth.jwt.JwtTokenProvider;
import com.smartspending.common.redis.RedisService;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Long userId = userDetails.getUserId();
        String email = userDetails.getEmail();
        Map<String, Object> attributes = userDetails.getAttributes();

        String accessToken = jwtTokenProvider.createAccessToken(userId, email, attributes);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, email);

        redisService.saveRefreshToken(userId, refreshToken);

        // 응답 객체 생성
        Map<String, String> tokens = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
        CommonResponse<Map<String, String>> successResponse = ApiResponseUtil.success(tokens);

        // 응답 작성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));

    }
}

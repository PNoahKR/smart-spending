package com.smartspending.common.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.response.CommonResponse;
import com.smartspending.common.util.ApiResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2FailHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 공통 응답 생성
        CommonResponse<Void> failureResponse = ApiResponseUtil.failure(CommonResponseCode.UNAUTHORIZED_USER);

        // 응답 작성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(failureResponse));
    }
}

package com.smartspending.common.auth.oauth2;

import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.auth.jwt.JwtTokenProvider;
import com.smartspending.common.redis.RedisService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            Long userId = userDetails.getUserId();
            String email = userDetails.getEmail();
            Map<String, Object> attributes = userDetails.getAttributes();

            String accessToken = jwtTokenProvider.createAccessToken(userId, email, attributes);
            String refreshToken = jwtTokenProvider.createRefreshToken(userId, email);

            redisService.saveRefreshToken(userId, refreshToken);

            String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/success")
                    .queryParam("access", accessToken)
                    .queryParam("refresh", refreshToken)
                    .build().toUriString();

            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            log.error("OAuth2 Success Handler 실패 : ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}

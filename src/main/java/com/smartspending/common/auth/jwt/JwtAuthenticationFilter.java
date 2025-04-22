package com.smartspending.common.auth.jwt;

import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.common.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Invalid JWT token");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (redisService.checkBlackList(token)) {
            log.debug("JWT token is blacklisted");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (jwtTokenProvider.validateToken(token)) {
            log.debug("JWT token validated");
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            String email = jwtTokenProvider.getEmailFromToken(token);

            Map<String, Object> attributes = jwtTokenProvider.getAttributesFromToken(token);
            UserDetailsImpl userDetails;
            if (attributes != null && !attributes.isEmpty()) {
                String name = attributes.get("name").toString();
                userDetails = new UserDetailsImpl(userId, email, name, attributes);
            } else {
                userDetails = new UserDetailsImpl(userId, email);
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}

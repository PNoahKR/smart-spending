package com.smartspending.common.auth.jwt;

import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token-expire-time}")
    private long accessExpirationTime;

    @Value("${jwt.refresh-token-expire-time}")
    private long refreshExpirationTime;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userId, String email, Map<String, Object> attributes) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(email).setId(String.valueOf(userId));
        if (attributes != null) {
            claims.putAll(attributes);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpirationTime))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken(Long userId, String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(email)
                .setId(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpirationTime))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new CustomException(CommonResponseCode.NOT_FOUND_JWT_TOKEN);
        }
        return header.substring(7);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.debug("JWT 검증 실패 : " + e.getMessage());
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(claims.getId());
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getExpirationTime(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        long expiration = claims.getExpiration().getTime(); // 만료 시간 (밀리초)
        long currentTime = System.currentTimeMillis(); // 현재 시간 (밀리초)
        return String.valueOf(expiration - currentTime); // 남은 유효 시간
    }

    public Map<String, Object> getAttributesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Map<String, Object> attributes = new HashMap<>();
        claims.forEach((key, value) -> {
            if (!"sub".equals(key) && !"jti".equals(key) && !"iat".equals(key) && !"exp".equals(key)) {
                attributes.put(key, value);
            }
        });
        return attributes;
    }
}

package com.smartspending.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set("refreshToken: " + userId, refreshToken, 7, TimeUnit.DAYS );
    }

    public String getRefreshToken(Long userId) {
        return (String) redisTemplate.opsForValue().get("refreshToken: " + userId);
    }

    public void setBlackList(String token) {
        redisTemplate.opsForSet().add("blackList: " + token, token);
    }

    public void removeRefreshToken(Long userId) {
        redisTemplate.delete( "refreshToken: " + userId );
    }

    public boolean checkRefreshToken(Long userId, String refreshToken) {
        String storedRefreshToken = (String) redisTemplate.opsForValue().get("refreshToken: " + userId);
        return storedRefreshToken != null && refreshToken.equals(storedRefreshToken);
    }

    public boolean checkBlackList(String token) {
        String storedRefreshToken = (String) redisTemplate.opsForValue().get("blackList: " + token);
        return storedRefreshToken != null && storedRefreshToken.equals(token);
    }
}

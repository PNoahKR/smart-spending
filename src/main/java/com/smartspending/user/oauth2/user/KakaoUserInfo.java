package com.smartspending.user.oauth2.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfo {
    private Map<String, Object> account;
    private Map<String, Object> profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.account = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) account.get("profile");
    }

    public String getEmail() {
        return (String) account.get("email");
    }

    public String getName() {
        return String.valueOf(profile.get("nickname"));
    }
}

package com.smartspending.common.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserDetailsImpl implements UserDetails, OAuth2User {

    private final Long userId;
    private final String email;
    private final String name;
    private final Map<String, Object> attributes;

    public UserDetailsImpl(Long userId, String email) {
        this.userId = userId;
        this.email = email;
        this.name = null;
        this.attributes = Collections.emptyMap();
    }

    public UserDetailsImpl(Long userId, String email, String name, Map<String, Object> attributes) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.attributes = attributes;
    }


    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

}

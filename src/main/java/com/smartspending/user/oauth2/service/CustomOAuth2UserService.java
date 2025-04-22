package com.smartspending.user.oauth2.service;

import com.smartspending.common.auth.UserDetailsImpl;
import com.smartspending.user.entity.User;
import com.smartspending.user.enums.Provider;
import com.smartspending.user.oauth2.user.GoogleUserInfo;
import com.smartspending.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registerId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        if (registerId.equals("google")) {
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);
            String email = googleUserInfo.getEmail();
            String name = googleUserInfo.getName();
            String providerId = attributes.get(userNameAttributeName).toString();

            User user = userRepository.findByEmail(email).orElseGet(() -> saveSocialUser(email, name, registerId, providerId));

            return new UserDetailsImpl(user.getId(), user.getEmail(), user.getName(), attributes);
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registerId);
        }
    }

    private User saveSocialUser(String email, String name, String registerId, String providerId) {
        Provider provider = null;

        String password = randomPassword();

        if (registerId.equals(Provider.GOOGLE.toString().toLowerCase())) {
            provider = Provider.GOOGLE;
        }

        User user = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .provider(provider)
                .providerId(providerId)
                .emailVerified(true)
                .build();

        return userRepository.save(user);
    }

    private String randomPassword() {
        String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            password.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password.toString());
    }
}

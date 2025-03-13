package com.smartspending.user.service;

import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.RegisterRequestDto;
import com.smartspending.user.entity.User;
import com.smartspending.user.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Long registerUser(RegisterRequestDto requestDto) {
        validateEmail(requestDto.getEmail());
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodePassword)
                .name(requestDto.getName())
                .emailVerified(requestDto.isEmailVerified())
                .build();
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public Long login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(CommonResponseCode.USER_NOT_FOUND);
        }
        return user.getId();
    }

    private void validateEmail(@NotBlank(message = "이메일을 입력해주세요") @Email(message = "올바른 형식이 아닙니다") String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(CommonResponseCode.EMAIL_ALREADY_EXISTS);
        }
    }
}

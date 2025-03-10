package com.smartspending.user.service;

import com.smartspending.user.dto.request.RegisterRequestDto;
import com.smartspending.user.entity.User;
import com.smartspending.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long registerUser(RegisterRequestDto requestDto) {
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .name(requestDto.getName())
                .emailVerified(requestDto.isEmailVerified())
                .build();
        userRepository.save(user);
        return user.getId();
    }
}

package com.smartspending.user.service;

import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.common.jwt.JwtTokenProvider;
import com.smartspending.common.redis.RefreshTokenService;
import com.smartspending.user.dto.request.LoginRequestDto;
import com.smartspending.user.dto.request.RegisterRequestDto;
import com.smartspending.user.dto.request.RequestTokenDto;
import com.smartspending.user.dto.response.LoginResponseDto;
import com.smartspending.user.entity.User;
import com.smartspending.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

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
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(CommonResponseCode.USER_NOT_FOUND);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public LoginResponseDto reCreateAccessToken(RequestTokenDto requestDto) {

        Long userId = jwtTokenProvider.getUserIdFromToken(requestDto.getAccessToken());

        if(!refreshTokenService.checkRefreshToken(userId, requestDto.getRefreshToken())) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }

        String refreshToken = refreshTokenService.getRefreshToken(userId);

        if (!refreshToken.equals(requestDto.getRefreshToken())) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }

        String accessToken = jwtTokenProvider.createAccessToken(userId);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.getJwtFromHeader(request);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        refreshTokenService.removeRefreshToken(userId);
        refreshTokenService.setBlackList(accessToken);
    }

    private void validateEmail(@NotBlank(message = "이메일을 입력해주세요") @Email(message = "올바른 형식이 아닙니다") String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(CommonResponseCode.EMAIL_ALREADY_EXISTS);
        }
    }
}

package com.smartspending.user.service;

import com.smartspending.common.auth.jwt.JwtTokenProvider;
import com.smartspending.common.exception.CommonResponseCode;
import com.smartspending.common.exception.CustomException;
import com.smartspending.common.redis.RedisService;
import com.smartspending.user.dto.request.*;
import com.smartspending.user.dto.response.LoginResponseDto;
import com.smartspending.user.entity.User;
import com.smartspending.user.enums.Provider;
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
    private final RedisService redisService;
    private final MailService mailService;

    @Override
    public boolean duplicateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return false;
        } else {
            validateEmail(email);
            return true;
        }
    }

    @Override
    public void sendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));
        if (!user.getProvider().equals(Provider.LOCAL)) {
            throw new CustomException(CommonResponseCode.USER_NOT_FOUND);
        }
        String code = mailService.verifyCode();
        redisService.removeVerificationCode(email); // 이전 실패 경험 데이터 삭제
        redisService.saveVerificationCode(email, code);

        mailService.sendVerificationEmail(email, code);
    }

    @Override
    public boolean verifyEmail(VerifyCodeRequestDto requestDto) {
        String verificationCode = redisService.getVerificationCode(requestDto.getEmail());
        if (verificationCode == null || !verificationCode.equals(requestDto.getCode())) {
            throw new CustomException(CommonResponseCode.EMAIL_NOT_VERIFIED);
        } else {
            redisService.removeVerificationCode(requestDto.getEmail());
            return true;
        }
    }

    @Override
    @Transactional
    public Long register(RegisterRequestDto requestDto) {
        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodePassword)
                .name(requestDto.getName())
                .emailVerified(true)
                .build();
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));

        if (!user.getProvider().equals(Provider.LOCAL)) {
            throw new CustomException(CommonResponseCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(CommonResponseCode.USER_NOT_FOUND);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId(), user.getEmail());

        redisService.saveRefreshToken(user.getId(), refreshToken);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto requestDto) {
        String newPassword = requestDto.getNewPassword();
        String confirmPassword = requestDto.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)) {
            throw new CustomException(CommonResponseCode.PASSWORD_MISMATCH);
        }
        String encodePassword = passwordEncoder.encode(requestDto.getNewPassword());
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(CommonResponseCode.USER_NOT_FOUND));
        user.updatePassword(encodePassword);
    }

    @Override
    public LoginResponseDto reCreateAccessToken(RequestTokenDto requestDto) {

        Long userId = jwtTokenProvider.getUserIdFromToken(requestDto.getAccessToken());
        String email = jwtTokenProvider.getEmailFromToken(requestDto.getAccessToken());

        if(!redisService.checkRefreshToken(userId, requestDto.getRefreshToken())) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }

        String refreshToken = redisService.getRefreshToken(userId);

        if (!refreshToken.equals(requestDto.getRefreshToken())) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }

        String accessToken = jwtTokenProvider.createAccessToken(userId, email);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.getJwtFromHeader(request);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(CommonResponseCode.UNAUTHORIZED_USER);
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
        redisService.removeRefreshToken(userId);
        String expirationTime = jwtTokenProvider.getExpirationTime(accessToken);
        redisService.setBlackList(accessToken, expirationTime);
    }

    private void validateEmail(@NotBlank(message = "이메일을 입력해주세요") @Email(message = "올바른 형식이 아닙니다") String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(CommonResponseCode.EMAIL_ALREADY_EXISTS);
        }
    }
}

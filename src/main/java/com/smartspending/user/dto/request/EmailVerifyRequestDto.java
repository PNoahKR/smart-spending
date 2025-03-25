package com.smartspending.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyRequestDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 형식이 아닙니다")
    private String email;
}

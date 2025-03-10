package com.smartspending.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    private String email;
    private String password;
    private String name;
    private boolean emailVerified = false;
}

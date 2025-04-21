package com.smartspending.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Provider {

    LOCAL("일반회원"), GOOGLE("구글"), KAKAO("카카오"), NAVER("네이버");

    private final String provider;
}

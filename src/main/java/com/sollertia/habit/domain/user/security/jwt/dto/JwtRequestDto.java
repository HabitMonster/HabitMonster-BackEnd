package com.sollertia.habit.domain.user.security.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtRequestDto {
    private String accessToken;
    private String refreshToken;
}

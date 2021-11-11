package com.sollertia.habit.config.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtRequestDto {
    private String accessToken;
    private String refreshToken;
}

package com.sollertia.habit.config.jwt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtRequestDto {
    private String accesstoken;
    private String refreshtoken;
}

package com.sollertia.habit.config.jwt.dto;

import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class JwtResponseDto extends DefaultResponseDto {

    private String accessToken;
    private String refreshToken;
    private Boolean isFirstLogin;
}

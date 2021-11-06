package com.sollertia.habit.domain.user.security.jwt.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class JwtResponseDto extends DefaultResponseDto {

    private String accessToken;
    private String refreshToken;
    private Boolean isFirstLogin;
}

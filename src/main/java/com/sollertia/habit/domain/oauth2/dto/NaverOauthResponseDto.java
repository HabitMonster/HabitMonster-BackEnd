package com.sollertia.habit.domain.oauth2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NaverOauthResponseDto {
    private String token_type;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String error;
    private String error_description;
}

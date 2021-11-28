package com.sollertia.habit.domain.user.oauth2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoOauthResponseDto {
    private String token_type;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String scope;
}

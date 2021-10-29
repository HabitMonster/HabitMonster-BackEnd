package com.sollertia.habit.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDto {

    private String accesstoken;
    private String refreshtoken;
    private Boolean isFirstLongin;
    private String message;

}

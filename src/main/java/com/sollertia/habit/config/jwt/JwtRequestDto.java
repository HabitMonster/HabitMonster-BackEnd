package com.sollertia.habit.config.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtRequestDto {

    private String accesstoken;
    private String refreshtoken;
    private Boolean isFirstLongin;

    public JwtRequestDto(String accesstoken, String refreshtoken, Boolean isFirstLongin) {
            this.accesstoken = accesstoken;
            this.refreshtoken = refreshtoken;
            this.isFirstLongin = isFirstLongin;
    }
}

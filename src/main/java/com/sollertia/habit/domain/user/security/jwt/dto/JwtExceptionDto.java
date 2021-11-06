package com.sollertia.habit.domain.user.security.jwt.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@SuperBuilder
public class JwtExceptionDto extends DefaultResponseDto {
    private String clientRequestUri;
    private Map<String,Object> body;
}

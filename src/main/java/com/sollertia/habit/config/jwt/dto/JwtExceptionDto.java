package com.sollertia.habit.config.jwt.dto;

import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@SuperBuilder
public class JwtExceptionDto extends DefaultResponseDto {
    private String clientRequestUri;
    private Map<String,Object> body;
}

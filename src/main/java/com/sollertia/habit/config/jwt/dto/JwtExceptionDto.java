package com.sollertia.habit.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtExceptionDto {
    private String message;
    private String clientRequestUri;
    private Map<String,Object> body;
}

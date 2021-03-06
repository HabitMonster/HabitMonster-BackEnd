package com.sollertia.habit.global.utils;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DefaultResponseDto {
    private Integer statusCode;
    private String responseMessage;

    protected DefaultResponseDto(Integer statusCode, String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }

    protected DefaultResponseDto() {
    }
}
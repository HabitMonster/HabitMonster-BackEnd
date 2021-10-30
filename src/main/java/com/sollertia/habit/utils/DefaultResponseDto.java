package com.sollertia.habit.utils;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class DefaultResponseDto {
    private int statusCode;
    private String responseMessage;

    protected DefaultResponseDto(int statusCode, String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }
}
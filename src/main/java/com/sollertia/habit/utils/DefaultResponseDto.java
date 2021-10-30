package com.sollertia.habit.utils;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DefaultResponseDto {
    private int statusCode;
    private String responseMessage;

    protected DefaultResponseDto(int statusCode, String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }

    protected DefaultResponseDto() {
    }
}
package com.sollertia.habit.utils;

public abstract class DefaultResponseDto {
    private int statusCode;
    private String responseMessage;

    public DefaultResponseDto(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }
}

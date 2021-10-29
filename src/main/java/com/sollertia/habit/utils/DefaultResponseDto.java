package com.sollertia.habit.utils;

public abstract class DefaultResponseDto {
    private int statusCode;
    private String responseMessage;

    protected DefaultResponseDto(int statusCode, String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }

    protected DefaultResponseDto(String responseMessage) {
        this.statusCode = 200;
        this.responseMessage = responseMessage;
    }
}
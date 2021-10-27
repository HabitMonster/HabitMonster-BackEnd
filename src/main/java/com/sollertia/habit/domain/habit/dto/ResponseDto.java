package com.sollertia.habit.domain.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDto {
    private Long statusCode;
    private String message;
}

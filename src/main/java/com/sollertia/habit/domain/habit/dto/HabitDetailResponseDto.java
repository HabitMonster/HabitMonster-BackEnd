package com.sollertia.habit.domain.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitDetailResponseDto {
    private HabitDetail habitDetail;
    private Long statusCode;
    private String message;
}

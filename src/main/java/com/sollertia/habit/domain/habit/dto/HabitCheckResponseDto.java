package com.sollertia.habit.domain.habit.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HabitCheckResponseDto extends DefaultResponseDto {
    private HabitSummaryDto habit;
}

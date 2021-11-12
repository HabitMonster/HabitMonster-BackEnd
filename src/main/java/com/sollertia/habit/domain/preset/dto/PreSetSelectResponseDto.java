package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PreSetSelectResponseDto extends DefaultResponseDto {
    private HabitDtoImpl habitDto;
}

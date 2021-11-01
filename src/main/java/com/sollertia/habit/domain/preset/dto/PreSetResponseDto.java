package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class PreSetResponseDto extends DefaultResponseDto {
    private List<PreSetVo> preSets;
    private HabitDtoImpl habitDto;
}

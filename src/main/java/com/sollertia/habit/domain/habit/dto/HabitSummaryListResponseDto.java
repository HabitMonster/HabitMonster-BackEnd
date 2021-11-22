package com.sollertia.habit.domain.habit.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class HabitSummaryListResponseDto extends DefaultResponseDto {
    private List<HabitSummaryVo> habits;
    private Integer totalHabitCount;
}

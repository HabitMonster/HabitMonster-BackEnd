package com.sollertia.habit.domain.habit.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class HabitSummaryResponseDto extends DefaultResponseDto {
    List<HabitSummaryVo> habits;
}

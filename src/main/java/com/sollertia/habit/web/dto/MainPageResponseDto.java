package com.sollertia.habit.web.dto;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class MainPageResponseDto extends DefaultResponseDto {
    private MonsterVo monster;
    private Long expPercentage;
    private List<HabitSummaryVo> habits;
}

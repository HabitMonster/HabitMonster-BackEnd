package com.sollertia.habit.domain.statistics.dto;

import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitDto;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class StatisticsResponseDto extends DefaultResponseDto {
    private int totalCount;
    private int succeededCount;
    private int failedCount;
    private List<SimpleHabitDto> habitList;
}

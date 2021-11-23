package com.sollertia.habit.domain.statistics.dto;

import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class GlobalStatisticsResponseDto extends DefaultResponseDto {
    private List<GlobalStatisticsVo> statistics;
}

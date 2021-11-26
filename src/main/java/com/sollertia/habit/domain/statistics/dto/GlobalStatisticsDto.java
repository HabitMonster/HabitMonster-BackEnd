package com.sollertia.habit.domain.statistics.dto;

import com.sollertia.habit.domain.statistics.entity.Statistics;
import lombok.Getter;

@Getter
public class GlobalStatisticsDto {
    private String content;
    private String value;

    private GlobalStatisticsDto(String content, String value) {
        this.content = content;
        this.value = value;
    }

    public static GlobalStatisticsDto of(Statistics statistics) {
        return new GlobalStatisticsDto(statistics.getContents(), statistics.getValue());
    }
}

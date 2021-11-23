package com.sollertia.habit.domain.statistics.dto;

import com.sollertia.habit.domain.statistics.entity.Statistics;
import lombok.Getter;

@Getter
public class GlobalStatisticsVo {
    private String content;

    private GlobalStatisticsVo(String content) {
        this.content = content;
    }

    public static GlobalStatisticsVo of(Statistics statistics) {
        return new GlobalStatisticsVo(statistics.getContents());
    }
}

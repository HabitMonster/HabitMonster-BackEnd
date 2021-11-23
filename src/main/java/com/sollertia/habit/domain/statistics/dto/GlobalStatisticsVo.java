package com.sollertia.habit.domain.statistics.dto;

import com.sollertia.habit.domain.statistics.entity.Statistics;
import lombok.Getter;

@Getter
public class GlobalStatisticsVo {
    private String content;
    private String value;

    private GlobalStatisticsVo(String content, String value) {
        this.content = content;
        this.value = value;
    }

    public static GlobalStatisticsVo of(Statistics statistics) {
        return new GlobalStatisticsVo(statistics.getContents(), statistics.getValue());
    }
}

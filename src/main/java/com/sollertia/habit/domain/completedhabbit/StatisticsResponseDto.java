package com.sollertia.habit.domain.completedhabbit;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class StatisticsResponseDto {

    private int totalCount;
    private int succeededCount;
    private int failedCount;
    private List<CompletedHabit> successList;
    private List<CompletedHabit> failedList;
    private int statusCode;
    private String msg;

    public StatisticsResponseDto(List<CompletedHabit> successList, List<CompletedHabit> failedList) {
        this.successList = successList;
        this.failedList = failedList;
        this.totalCount = successList.size() + failedList.size();
        this.succeededCount = successList.size();
        this.failedCount = failedList.size();
    }

}

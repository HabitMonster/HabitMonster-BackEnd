package com.sollertia.habit.domain.completedhabbit.statics.dto;

import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitVo;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class StatisticsResponseDto {

    private int totalCount;
    private int succeededCount;
    private int failedCount;
    private List<SimpleHabitVo> successList;
    private List<SimpleHabitVo> failedList;
    private int statusCode;
    private String msg;

    public StatisticsResponseDto(List<SimpleHabitVo> successList, List<SimpleHabitVo> failedList) {
        this.successList = successList;
        this.failedList = failedList;
        this.totalCount = successList.size() + failedList.size();
        this.succeededCount = successList.size();
        this.failedCount = failedList.size();
    }

}

package com.sollertia.habit.domain.statistics.dto;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.Getter;

@Getter
public class StatisticsSuccessCategoryAvgVo {
    private Category category;
    private Double avgPer;
}

package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.util.List;

public interface HabitRepositoryCustom {
    List<StatisticsCategoryVo> statisticsMaxSelectedByCategory(SearchDateDto duration);
    List<Long> statisticsGetNumberOfUser();
}

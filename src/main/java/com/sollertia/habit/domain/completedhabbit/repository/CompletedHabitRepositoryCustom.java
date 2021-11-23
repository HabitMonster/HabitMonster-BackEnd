package com.sollertia.habit.domain.completedhabbit.repository;

import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.time.LocalDate;
import java.util.List;

public interface CompletedHabitRepositoryCustom {
    List<StatisticsSuccessCategoryAvgVo> statisticsAvgAchievementPercentageByCategory(SearchDateDto duration);
    List<StatisticsCategoryVo> statisticsMaxSelectedByCategory(LocalDate start, LocalDate end);
}

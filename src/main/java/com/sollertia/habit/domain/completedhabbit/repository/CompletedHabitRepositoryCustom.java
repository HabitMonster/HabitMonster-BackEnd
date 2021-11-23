package com.sollertia.habit.domain.completedhabbit.repository;

import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CompletedHabitRepositoryCustom {
    List<StatisticsSuccessCategoryAvgVo> statisticsAvgAchievementPercentageByCategory(LocalDateTime start, LocalDateTime end);
    List<StatisticsCategoryVo> statisticsMaxSelectedByCategory(LocalDate start, LocalDate end);
}

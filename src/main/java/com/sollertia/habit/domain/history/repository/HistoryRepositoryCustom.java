package com.sollertia.habit.domain.history.repository;

import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepositoryCustom {
    List<StatisticsCategoryVo> statisticsMonthMaxMinusByCategory(LocalDateTime start, LocalDateTime end);
}

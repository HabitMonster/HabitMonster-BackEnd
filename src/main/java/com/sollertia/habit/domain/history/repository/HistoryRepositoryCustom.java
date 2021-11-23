package com.sollertia.habit.domain.history.repository;

import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.util.List;

public interface HistoryRepositoryCustom {
    List<StatisticsCategoryVo> statisticsMonthMaxMinusByCategory(SearchDateDto duration);
}

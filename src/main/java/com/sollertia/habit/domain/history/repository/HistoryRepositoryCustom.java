package com.sollertia.habit.domain.history.repository;

import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface HistoryRepositoryCustom {

    Map<String, Integer> getMostFaildedDay(SearchDateDto searchDateDto);
    List<StatisticsCategoryVo> statisticsMonthMaxMinusByCategory(SearchDateDto duration);
}

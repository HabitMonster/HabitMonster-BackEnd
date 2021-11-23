package com.sollertia.habit.domain.history.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.global.OrderByNull;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sollertia.habit.domain.history.entity.QHistory.history;

@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsCategoryVo> statisticsMonthMaxMinusByCategory(SearchDateDto duration) {
        return jpaQueryFactory
                .select(
                        Projections.fields(StatisticsCategoryVo.class,
                                history.category,
                                history.category.count().as("num"))
                )
                .from(history)
                .where(history.isSuccessToday.eq(false).and(history.endUpDateTime.between(duration.getSearchStartDate(),duration.getSearchEndDate())))
                .groupBy(history.category)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }
}

package com.sollertia.habit.domain.history.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.global.OrderByNull;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sollertia.habit.domain.history.entity.QHistory.history;

@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsCategoryVo> statisticsMonthMaxMinusByCategory(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(
                        Projections.fields(StatisticsCategoryVo.class,
                                history.category,
                                history.category.count().as("num"))
                )
                .from(history)
                .where(history.isSuccessToday.eq(false).and(history.endUpDateTime.between(start,end)))
                .groupBy(history.category)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }

    @Override
    public Map<String, Integer> getMostFaildedDay(SearchDateDto searchDateDto) {
        List<Tuple> result = jpaQueryFactory
                .select(history.dayOfWeek, history.id.count())
                .where(history.createdAt.between(searchDateDto.getSearchStartDate(), searchDateDto.getSearchEndDate()),
                        history.isSuccessToday.eq(false))
                .groupBy(history.dayOfWeek)
                .fetch();
        return resultToHashMap(result);
    }

    private Map<String, Integer> resultToHashMap(List<Tuple> result) {
        Map<String, Integer> resultMap = new HashMap<>();
        for (Tuple tuple : result) {
            resultMap.put(tuple.get(0, String.class), tuple.get(1, Integer.class));
        }
        return resultMap;
    }
}

package com.sollertia.habit.domain.completedhabbit.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.global.OrderByNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sollertia.habit.domain.completedhabbit.entity.QCompletedHabit.completedHabit;

@RequiredArgsConstructor
public class CompletedHabitRepositoryImpl implements  CompletedHabitRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsSuccessCategoryAvgVo> statisticsAvgAchievementPercentageByCategory(LocalDateTime start, LocalDateTime end) {
        return jpaQueryFactory
                .select(
                        Projections.fields(StatisticsSuccessCategoryAvgVo.class,
                                completedHabit.category,
                                completedHabit.achievementPercentage.avg().as("avgPer"))
                )
                .from(completedHabit)
                .where(completedHabit.isSuccess.eq(true).and(completedHabit.createdAt.between(start,end)))
                .groupBy(completedHabit.category)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }

    @Override
    public List<StatisticsCategoryVo> statisticsMaxSelectedByCategory(LocalDate start, LocalDate end) {
        return jpaQueryFactory
                .select(
                        Projections.fields(StatisticsCategoryVo.class,
                                completedHabit.category,
                                completedHabit.category.count().as("num"))
                )
                .from(completedHabit)
                .where((completedHabit.startDate.between(start,end)))
                .groupBy(completedHabit.category)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }


}

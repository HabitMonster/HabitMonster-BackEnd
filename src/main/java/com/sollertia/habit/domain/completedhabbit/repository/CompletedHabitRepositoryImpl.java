package com.sollertia.habit.domain.completedhabbit.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.global.OrderByNull;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.sollertia.habit.domain.completedhabbit.entity.QCompletedHabit.completedHabit;

@RequiredArgsConstructor
public class CompletedHabitRepositoryImpl implements  CompletedHabitRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsSuccessCategoryAvgVo> statisticsAvgAchievementPercentageByCategory(SearchDateDto duration) {
        return jpaQueryFactory
                .select(
                        Projections.fields(StatisticsSuccessCategoryAvgVo.class,
                                completedHabit.category,
                                completedHabit.achievementPercentage.avg().as("avgPer"))
                )
                .from(completedHabit)
                .where(completedHabit.isSuccess.eq(true).and(completedHabit.createdAt.between(duration.getSearchStartDate(),duration.getSearchEndDate())))
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
                .where((completedHabit.startDate.between(start, end)))
                .groupBy(completedHabit.category)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }

    @Override
    public List<CompletedHabit> habitMoreThanAvgAchievementPercentageByCategory(Category category, Long achievementPercentage) {
        return jpaQueryFactory
                .selectFrom(completedHabit)
                .where(completedHabit.category.eq(category).and(completedHabit.achievementPercentage.goe(achievementPercentage)))
                .fetch();
    }


}

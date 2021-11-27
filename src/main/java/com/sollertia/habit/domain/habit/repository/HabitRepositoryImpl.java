package com.sollertia.habit.domain.habit.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.global.OrderByNull;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sollertia.habit.domain.habit.entity.QHabit.habit;

@RequiredArgsConstructor
public class HabitRepositoryImpl implements HabitRepositoryCustom{
   // select count(user)  from habit  group by user;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StatisticsCategoryVo> statisticsMaxSelectedByCategory(SearchDateDto duration) {
        return jpaQueryFactory
                .select(
                        Projections.fields(StatisticsCategoryVo.class,
                                habit.category,
                                habit.category.count().as("num"))
                )
                .from(habit)
                .where((habit.createdAt.between(duration.getSearchStartDate(), duration.getSearchEndDate())))
                .groupBy(habit.category)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }

    @Override
    public List<Long> statisticsGetNumberOfUser() {
        return jpaQueryFactory.select(habit.user.count()).from(habit).groupBy(habit.user).orderBy(OrderByNull.DEFAULT).fetch();
    }
}

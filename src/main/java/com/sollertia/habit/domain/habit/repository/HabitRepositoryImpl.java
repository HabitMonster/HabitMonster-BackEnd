package com.sollertia.habit.domain.habit.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HabitRepositoryImpl implements HabitRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
}

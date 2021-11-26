package com.sollertia.habit.domain.monster.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MonsterRepositoryImpl implements MonsterRepositoryCustom{

    private final JPAQueryFactory queryFactory;
}

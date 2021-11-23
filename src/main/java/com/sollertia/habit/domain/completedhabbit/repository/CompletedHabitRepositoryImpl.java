package com.sollertia.habit.domain.completedhabbit.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class CompletedHabitRepositoryImpl implements  CompletedHabitRepositoryCustom{

    @Autowired
    EntityManager em;

    private final QueryFactory queryFactory;

    public CompletedHabitRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


}

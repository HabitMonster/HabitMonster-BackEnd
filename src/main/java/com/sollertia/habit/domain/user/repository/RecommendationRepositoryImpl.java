package com.sollertia.habit.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.entity.Recommendation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sollertia.habit.domain.user.entity.QRecommendation.recommendation;
import static com.sollertia.habit.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Recommendation> searchByNumber(int number) {
        return queryFactory
                .selectFrom(recommendation)
                .join(recommendation.user, user).fetchJoin()
                .where(recommendation.number.eq(number))
                .fetch();
    }
}

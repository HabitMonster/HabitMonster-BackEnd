package com.sollertia.habit.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.sollertia.habit.domain.user.entity.QRecommendation.recommendation;
import static com.sollertia.habit.domain.user.entity.QUser.user;

import com.sollertia.habit.domain.user.entity.Recommendation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RecommendationRepositoryImpl implements RecommendationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RecommendationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Recommendation> searchByNumber(int number) {
        return queryFactory
                .selectFrom(recommendation)
                .join(recommendation.user, user).fetchJoin()
                .where(recommendation.number.eq(number))
                .fetch();
    }
}

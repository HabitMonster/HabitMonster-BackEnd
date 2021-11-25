package com.sollertia.habit.domain.user.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.dto.QRecommendationVo;
import com.sollertia.habit.domain.user.dto.RecommendationVo;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.QFollowVo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sollertia.habit.domain.monster.entity.QMonster.monster;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.monsterDatabase;
import static com.sollertia.habit.domain.user.entity.QRecommendation.recommendation;
import static com.sollertia.habit.domain.user.entity.QUser.user;
import static com.sollertia.habit.domain.user.follow.entity.QFollow.follow;

@Repository
public class RecommendationRepositoryImpl implements RecommendationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RecommendationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<RecommendationVo> searchByNumber(User login, int number) {
        return queryFactory
                .select(new QRecommendationVo(recommendation.type,
                        new QFollowVo(
                                user.username,
                                monsterDatabase.id,
                                monsterDatabase.imageUrl,
                                user.monsterCode,
                                new CaseBuilder()
                                        .when(follow.id.isNull())
                                        .then(Boolean.FALSE)
                                        .otherwise(Boolean.TRUE))
                        ))
                .from(recommendation)
                .join(recommendation.user, user)
                .join(user.monster, monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .leftJoin(follow)
                    .on(user.eq(follow.following)
                            .and(follow.follower.eq(login))
                    )
                .where(recommendation.number.eq(number))
                .fetch();
    }
}

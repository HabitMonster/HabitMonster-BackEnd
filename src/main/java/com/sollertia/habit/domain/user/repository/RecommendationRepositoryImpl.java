package com.sollertia.habit.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.dto.QRecommendationDto;
import com.sollertia.habit.domain.user.dto.RecommendationDto;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.QFollowDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sollertia.habit.domain.monster.entity.QMonster.monster;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.monsterDatabase;
import static com.sollertia.habit.domain.user.entity.QRecommendation.recommendation;
import static com.sollertia.habit.domain.user.entity.QUser.user;
import static com.sollertia.habit.domain.user.follow.entity.QFollow.follow;
import com.sollertia.habit.domain.user.entity.Recommendation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sollertia.habit.domain.user.entity.QRecommendation.recommendation;
import static com.sollertia.habit.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecommendationDto> searchByNumber(User login, int number) {
        return queryFactory
                .select(new QRecommendationDto(recommendation.type,
                        new QFollowDto(
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
                .where(recommendation.number.eq(number)
                        .and(user.disabled.eq(false)))
                .fetch();
    }
}

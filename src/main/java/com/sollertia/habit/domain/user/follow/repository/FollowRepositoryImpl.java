package com.sollertia.habit.domain.user.follow.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowVo;
import com.sollertia.habit.domain.user.follow.dto.QFollowVo;
import com.sollertia.habit.domain.user.follow.entity.QFollow;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sollertia.habit.domain.monster.entity.QMonster.monster;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.monsterDatabase;
import static com.sollertia.habit.domain.user.entity.QUser.user;
import static com.sollertia.habit.domain.user.follow.entity.QFollow.follow;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FollowVo> followers(Long followerId) {
        return null;
        //return queryFactory.select(Projections.class,);
    }

    @Override
    public List<FollowVo> searchFollowersByUser(User login) {
        QFollow subFollow = new QFollow("subFollow");
        return queryFactory
                .select(new QFollowVo(
                        user.username,
                        monsterDatabase.id,
                        monsterDatabase.imageUrl,
                        user.monsterCode,
                                new CaseBuilder()
                                        .when(subFollow.id.isNull())
                                        .then(Boolean.FALSE)
                                        .otherwise(Boolean.TRUE))
                        )
                .from(follow)
                .join(follow.follower, user)
                .join(user.monster, monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .where(follow.following.eq(login))
                .leftJoin(subFollow)
                .on(subFollow.follower.eq(login)
                        .and(subFollow.following.eq(user)))
                .fetch();
    }

    @Override
    public List<FollowVo> searchFollowersByUser(User login, User target) {
        QFollow subFollow = new QFollow("subFollow");
        return queryFactory
                .select(new QFollowVo(
                        user.username,
                        monsterDatabase.id,
                        monsterDatabase.imageUrl,
                        user.monsterCode,
                        new CaseBuilder()
                                .when(subFollow.id.isNotNull())
                                .then(Boolean.TRUE)
                                .when(follow.follower.eq(login))
                                .then(Expressions.nullExpression())
                                .otherwise(Boolean.FALSE))
                )
                .from(follow)
                .join(follow.follower, user)
                .join(user.monster, monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .where(follow.following.eq(target))
                .leftJoin(subFollow)
                .on(subFollow.follower.eq(login)
                        .and(subFollow.following.eq(user)))
                .fetch();
    }

    @Override
    public List<FollowVo> searchFollowingsByUser(User login) {
        return queryFactory
                .select(new QFollowVo(
                        user.username,
                        monsterDatabase.id,
                        monsterDatabase.imageUrl,
                        user.monsterCode,
                        Expressions.TRUE
                ))
                .from(follow)
                .join(follow.following, user)
                .join(user.monster, monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .where(follow.follower.eq(login))
                .fetch();
    }

    @Override
    public List<FollowVo> searchFollowingsByUser(User login, User target) {
        QFollow subFollow = new QFollow("subFollow");
        return queryFactory
                .select(new QFollowVo(
                        user.username,
                        monsterDatabase.id,
                        monsterDatabase.imageUrl,
                        user.monsterCode,
                        new CaseBuilder()
                                .when(subFollow.id.isNotNull())
                                .then(Boolean.TRUE)
                                .when(follow.following.eq(login))
                                .then(Expressions.nullExpression())
                                .otherwise(Boolean.FALSE))
                )
                .from(follow)
                .join(follow.following, user)
                .join(user.monster, monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .where(follow.follower.eq(target))
                .leftJoin(subFollow)
                .on(subFollow.follower.eq(login)
                        .and(subFollow.following.eq(user)))
                .fetch();
    }
}

package com.sollertia.habit.domain.user.follow.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowDto;
import com.sollertia.habit.domain.user.follow.dto.QFollowDto;
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
    public List<FollowDto> searchFollowersByUser(User login) {
        QFollow subFollow = new QFollow("subFollow");
        return queryFactory
                .select(new QFollowDto(
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
                .where(follow.following.eq(login)
                        .and(user.disabled.eq(false)))
                .leftJoin(subFollow)
                .on(subFollow.follower.eq(login)
                        .and(subFollow.following.eq(user)))
                .fetch();
    }

    @Override
    public List<FollowDto> searchFollowersByUser(User login, User target) {
        QFollow subFollow = new QFollow("subFollow");
        return queryFactory
                .select(new QFollowDto(
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
                .where(follow.following.eq(target)
                        .and(user.disabled.eq(false)))
                .leftJoin(subFollow)
                .on(subFollow.follower.eq(login)
                        .and(subFollow.following.eq(user)))
                .fetch();
    }

    @Override
    public List<FollowDto> searchFollowingsByUser(User login) {
        return queryFactory
                .select(new QFollowDto(
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
                .where(follow.follower.eq(login)
                        .and(user.disabled.eq(false)))
                .fetch();
    }

    @Override
    public List<FollowDto> searchFollowingsByUser(User login, User target) {
        QFollow subFollow = new QFollow("subFollow");
        return queryFactory
                .select(new QFollowDto(
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
                .leftJoin(subFollow)
                        .on(subFollow.follower.eq(login)
                .and(subFollow.following.eq(user)))
                .where(follow.follower.eq(target)
                        .and(user.disabled.eq(false)))
                .fetch();
    }

    @Override
    public FollowDto searchUser(String monsterCode, User login) {
        return queryFactory
                .select(new QFollowDto(
                        user.username,
                        monsterDatabase.id,
                        monsterDatabase.imageUrl,
                        user.monsterCode,
                        new CaseBuilder()
                                .when(follow.id.isNotNull())
                                .then(Boolean.TRUE)
                                .when(user.eq(login))
                                .then(Expressions.nullExpression())
                                .otherwise(Boolean.FALSE))
                )
                .from(user)
                .join(user.monster, monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .leftJoin(follow)
                .on(follow.follower.eq(login)
                        .and(follow.following.eq(user)))
                .where(user.monsterCode.eq(monsterCode)
                        .and(user.disabled.eq(false)))

                .fetchOne();
    }

    @Override
    public long countByFollower(User follower) {
        return queryFactory
                .selectFrom(follow)
                .join(follow.following, user)
                .where(follow.follower.eq(follower)
                        .and(user.disabled.eq(false)))
                .fetchCount();
    }

    @Override
    public long countByFollowing(User following) {
        return queryFactory
                .selectFrom(follow)
                .join(follow.follower, user)
                .where(follow.following.eq(following)
                        .and(user.disabled.eq(false)))
                .fetchCount();
    }
}

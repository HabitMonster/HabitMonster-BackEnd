package com.sollertia.habit.domain.user.follow.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
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
    public List<FollowVo> searchFollowersByUser(User target) {
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
                .where(follow.following.eq(target))
                .leftJoin(subFollow)
                .on(subFollow.follower.eq(target)
                        .and(subFollow.following.eq(user)))
                .fetch();
    }
}

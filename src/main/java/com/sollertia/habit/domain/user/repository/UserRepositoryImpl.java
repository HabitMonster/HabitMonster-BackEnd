package com.sollertia.habit.domain.user.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.dto.QUserMonsterDto;
import com.sollertia.habit.domain.user.dto.UserMonsterDto;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import static com.sollertia.habit.domain.monster.entity.QMonster.monster;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.monsterDatabase;
import static com.sollertia.habit.domain.user.entity.QUser.user;
import static com.sollertia.habit.domain.user.follow.entity.QFollow.follow;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserMonsterDto userDetailByMonsterCode(String monsterCode, User login) {

        return queryFactory
                .select(new QUserMonsterDto(
                        user.monsterCode,
                        user.username,
                        user.email,
                        monsterDatabase.id,
                        monsterDatabase.monsterType,
                        monsterDatabase.imageUrl,
                        monster.name,
                        monster.level,
                        monster.expPoint,
                        monster.createdAt,
                        new CaseBuilder()
                                .when(follow.id.isNotNull())
                                .then(Boolean.TRUE)
                                .when(user.eq(login))
                                .then(Expressions.nullExpression())
                                .otherwise(Boolean.FALSE), user)
                )
                .from(user)
                .join(user.monster,monster)
                .join(monster.monsterDatabase,monsterDatabase)
                .where(user.monsterCode.eq(monsterCode))
                .leftJoin(follow)
                .on(follow.follower.eq(login)
                        .and(follow.following.eq(user))).fetchOne();
    }



    @Override
    public List<User> searchTop10ByCategory(Category category) {
        return queryFactory
                .selectFrom(user)
                .join(completedHabit)
                .on(completedHabit.user.eq(user))
                .where(categoryEq(category), user.disabled.isFalse())
                .groupBy(user)
                .orderBy(completedHabit.count().desc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression categoryEq(Category category) {
        return category == null ? null : completedHabit.category.eq(category);
    }

    @Override
    public List<User> searchTop10ByFollow() {
        return queryFactory
                .selectFrom(user)
                .join(follow)
                .on(follow.follower.eq(user))
                .where(user.disabled.isFalse())
                .groupBy(user)
                .orderBy(follow.count().desc())
                .limit(10)
                .fetch();
    }
}

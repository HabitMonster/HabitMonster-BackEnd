package com.sollertia.habit.domain.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sollertia.habit.domain.completedhabbit.entity.QCompletedHabit.completedHabit;
import static com.sollertia.habit.domain.user.entity.QUser.user;
import static com.sollertia.habit.domain.user.follow.entity.QFollow.follow;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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

package com.sollertia.habit.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sollertia.habit.domain.completedhabbit.entity.QCompletedHabit.completedHabit;
import static com.sollertia.habit.domain.user.entity.QUser.user;

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
                .where(completedHabit.category.eq(category))
                .groupBy(user)
                .orderBy(user.count().desc())
                .limit(5)
                .fetch();
    }
}

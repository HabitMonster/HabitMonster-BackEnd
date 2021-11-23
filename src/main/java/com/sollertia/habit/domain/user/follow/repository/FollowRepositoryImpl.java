package com.sollertia.habit.domain.user.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.user.follow.dto.FollowVo;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FollowVo> followers(Long followerId) {
        return null;
        //return queryFactory.select(Projections.class,);
    }
}

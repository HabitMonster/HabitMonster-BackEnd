package com.sollertia.habit.domain.user.follow.repository;

import com.sollertia.habit.domain.user.follow.dto.FollowVo;

import java.util.List;

public interface FollowRepositoryCustom {

    List<FollowVo> followers(Long followerId);




}

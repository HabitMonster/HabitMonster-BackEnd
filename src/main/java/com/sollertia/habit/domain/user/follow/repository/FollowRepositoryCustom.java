package com.sollertia.habit.domain.user.follow.repository;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowVo;

import java.util.List;

public interface FollowRepositoryCustom {

    List<FollowVo> searchFollowersByUser(User user);

    List<FollowVo> searchFollowersByUser(User login, User target);

    List<FollowVo> searchFollowingsByUser(User login);

    List<FollowVo> searchFollowingsByUser(User login, User target);

    FollowVo searchUser(String monsterCode, User target);

    long countByFollower(User following);

    long countByFollowing(User following);
}

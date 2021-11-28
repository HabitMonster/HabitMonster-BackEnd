package com.sollertia.habit.domain.user.follow.repository;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowDto;

import java.util.List;

public interface FollowRepositoryCustom {

    List<FollowDto> searchFollowersByUser(User user);

    List<FollowDto> searchFollowersByUser(User login, User target);

    List<FollowDto> searchFollowingsByUser(User login);

    List<FollowDto> searchFollowingsByUser(User login, User target);

    FollowDto searchUser(String monsterCode, User target);

    long countByFollower(User following);

    long countByFollowing(User following);
}

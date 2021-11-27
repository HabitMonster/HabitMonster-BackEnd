package com.sollertia.habit.domain.user.follow.service;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCheckDto;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseDto;

public interface FollowService {

    FollowResponseDto getFollowers(User user);

    FollowResponseDto getFollowings(User user);

    FollowCheckDto requestFollow(String followingId, User user);

    FollowCheckDto requestUnFollow(String followingId, User user);

    FollowSearchResponseDto searchFollowing(String followingId, User user);

    FollowCheckDto checkFollow(String followingId, User user);

    FollowCount getCountByUser(User targetUser);
}

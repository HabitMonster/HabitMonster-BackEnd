package com.sollertia.habit.domain.user.follow.service;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.global.utils.DefaultResponseDto;

public interface FollowService {

    FollowResponseDto getFollowerList(User user);

    FollowResponseDto getFollowingList(User user);

    DefaultResponseDto requestFollow(String followingId, User user);

    DefaultResponseDto requestUnFollow(String followingId, User user);


}

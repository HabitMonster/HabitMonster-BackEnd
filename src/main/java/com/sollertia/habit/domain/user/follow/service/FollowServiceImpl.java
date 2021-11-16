package com.sollertia.habit.domain.user.follow.service;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.domain.user.follow.dto.FollowVo;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    @Override
    public FollowResponseDto getFollowers(User user) {

        List<FollowVo> followers = followRepository.findAllByFollowingId(user.getId()).stream()
                .map(FollowVo::followerOf).collect(Collectors.toCollection(ArrayList::new));

        return FollowResponseDto.builder().followers(followers)
                .statusCode(200).responseMessage("Followers Query Completed").build();
    }

    @Override
    public FollowResponseDto getFollowings(User user) {

        List<FollowVo> followings = followRepository.findAllByFollowerId(user.getId()).stream()
                .map(FollowVo::followingOf).collect(Collectors.toCollection(ArrayList::new));

        return FollowResponseDto.builder().followings(followings)
                .statusCode(200).responseMessage("Followings Query Completed").build();
    }

    @Transactional
    @Override
    public DefaultResponseDto requestFollow(String followingId, User user) {

        User followingUser = userRepository.findBySocialId(followingId).orElseThrow(
                () -> new UserIdNotFoundException("NotFound MonsterCode")
        );

        Follow follow = Follow.create(user, followingUser);
        followRepository.save(follow);

        return DefaultResponseDto.builder().statusCode(200).responseMessage("Success Follow").build();
    }

    @Transactional
    @Override
    public DefaultResponseDto requestUnFollow(String followingId, User user) {

        User followingUser = userRepository.findBySocialId(followingId).orElseThrow(
                () -> new UserIdNotFoundException("NotFound MonsterCode")
        );

        followRepository.deleteByFollowerIdAndFollowingId(user.getId(), followingUser.getId());

        return DefaultResponseDto.builder().statusCode(200).responseMessage("Success UnFollow").build();
    }
}

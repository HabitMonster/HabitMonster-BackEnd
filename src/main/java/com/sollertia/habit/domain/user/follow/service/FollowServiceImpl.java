package com.sollertia.habit.domain.user.follow.service;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.*;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.FollowException;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class  FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    @Override
    public FollowResponseDto getFollowers(User user) {

        List<FollowVo> followers = followRepository.searchFollowersByUser(user);

        return FollowResponseDto.builder()
                .followers(followers)
                .statusCode(200)
                .responseMessage("Followers Query Completed")
                .build();
    }

    @Override
    public FollowResponseDto getFollowings(User user) {

        List<FollowVo> followings = followRepository.searchFollowingsByUser(user);

        return FollowResponseDto.builder()
                .followings(followings)
                .statusCode(200)
                .responseMessage("Followings Query Completed")
                .build();
    }

    public FollowResponseDto getFollowers(String monsterCode, User user) {
        User targetUser = getUserByMonsterCode(monsterCode);
        List<FollowVo> followers = followRepository.searchFollowersByUser(user, targetUser);

        return FollowResponseDto.builder()
                .followers(followers)
                .statusCode(200)
                .responseMessage("Followers Query Completed")
                .build();
    }

    public FollowResponseDto getFollowings(String monsterCode, User user) {
        User targetUser = getUserByMonsterCode(monsterCode);
        List<FollowVo> followings = followRepository.searchFollowingsByUser(user, targetUser);

        return FollowResponseDto.builder()
                .followings(followings)
                .statusCode(200)
                .responseMessage("Followings Query Completed")
                .build();
    }

    @Transactional
    @Override
    public FollowCheckDto requestFollow(String followingId, User user) {

        if(user.getMonsterCode().equals(followingId)){
            throw new FollowException("You can't follow yourself");
        }

        User followingUser = getUserByMonsterCode(followingId);

        if (isFollowBetween(user, followingUser)) {
            throw new FollowException("Already Follow");
        }

        Follow follow = Follow.create(user, followingUser);
        followRepository.save(follow);

        return FollowCheckDto.builder().isFollowed(true).statusCode(200).responseMessage("Success Follow").build();
    }

    @Transactional
    @Override
    public FollowCheckDto requestUnFollow(String followingId, User user) {

        User followingUser = getUserByMonsterCode(followingId);

        followRepository.deleteByFollowerIdAndFollowingId(user.getId(), followingUser.getId());

        return FollowCheckDto.builder().isFollowed(false).statusCode(200).responseMessage("Success UnFollow").build();
    }

    @Override
    public FollowSearchResponseDto searchFollowing(String followingId, User user) {
        Optional<User> searchUserOp = userRepository.findByMonsterCode(followingId);

        if(!searchUserOp.isPresent()){
            return FollowSearchResponseDto.builder().userInfo(null).statusCode(400).responseMessage("Not Found User").build();
        }

        User searchUser = searchUserOp.get();

        FollowSearchResponseVo followSearchResponseVo = FollowSearchResponseVo.of(searchUser, checkFollow(followingId, user).getIsFollowed());

        return FollowSearchResponseDto.builder().userInfo(followSearchResponseVo).statusCode(200).responseMessage("Search Completed").build();
    }

    @Override
    public FollowCheckDto checkFollow(String followingId, User user) {
        if ( user.getMonsterCode().equals(followingId) ) {
            FollowCheckDto.builder().isFollowed(null).statusCode(200).responseMessage("isFollowedMySelf").build();
        }
        User checkUser = getUserByMonsterCode(followingId);

        return isFollowBetween(user, checkUser) ?
                FollowCheckDto.builder().isFollowed(true).statusCode(200).responseMessage("isFollowedTrue").build() :
                FollowCheckDto.builder().isFollowed(false).statusCode(200).responseMessage("isFollowedFalse").build();
    }

    public boolean isFollowBetween(User user, User checkUser) {
        return followRepository.findByFollowerIdAndFollowingId(user.getId(), checkUser.getId()) != null;
    }

    public FollowCount getCountByUser(User targetUser) {
        Integer followersCount = followRepository.countByFollowing(targetUser);
        Integer followingsCount = followRepository.countByFollower(targetUser);
        return new FollowCount(followersCount, followingsCount);
    }

    private User getUserByMonsterCode(String monsterCode) {
        return userRepository.findByMonsterCode(monsterCode).orElseThrow(
                () -> new UserIdNotFoundException("Not Found MonsterCode")
        );
    }
}

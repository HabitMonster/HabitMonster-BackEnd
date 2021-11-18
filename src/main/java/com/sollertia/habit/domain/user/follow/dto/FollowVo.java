package com.sollertia.habit.domain.user.follow.dto;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowVo {

    private String email;
    private String monsterName;
    private String monsterImg;
    private String monsterCode;
    private Boolean isFollowed;

    public static FollowVo followerOf(Follow follower, Boolean checkFollow) {
        User followerUser = follower.getFollower();
        return FollowVo.builder()
                .email(followerUser.getEmail() != null ? followerUser.getEmail() : followerUser.getUsername())
                .monsterName(followerUser.getMonster().getName())
                .monsterCode(followerUser.getMonsterCode())
                .isFollowed(checkFollow)
                .monsterImg(followerUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    public static FollowVo followingOf(Follow following) {
        User followingUser = following.getFollowing();
        return FollowVo.builder()
                .email(followingUser.getEmail() != null ? followingUser.getEmail() : followingUser.getUsername())
                .monsterName(followingUser.getMonster().getName())
                .monsterCode(followingUser.getMonsterCode())
                .isFollowed(true)
                .monsterImg(followingUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    public static FollowVo followingOf(Follow following, Boolean checkFollow) {
        User followingUser = following.getFollowing();
        return FollowVo.builder()
                .email(followingUser.getEmail() != null ? followingUser.getEmail() : followingUser.getUsername())
                .monsterName(followingUser.getMonster().getName())
                .monsterCode(followingUser.getMonsterCode())
                .isFollowed(checkFollow)
                .monsterImg(followingUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }
}

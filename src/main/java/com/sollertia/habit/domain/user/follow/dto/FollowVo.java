package com.sollertia.habit.domain.user.follow.dto;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowVo {

    private String nickName;
    private Long monsterId;
    private String monsterImg;
    private String monsterCode;
    private Boolean isFollowed;

    public static FollowVo followerOf(Follow follower, Boolean checkFollow) {
        User followerUser = follower.getFollower();
        return FollowVo.builder()
                .nickName(followerUser.getUsername())
                .monsterId(followerUser.getMonster().getMonsterDatabase().getId())
                .monsterCode(followerUser.getMonsterCode())
                .isFollowed(checkFollow)
                .monsterImg(followerUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    public static FollowVo followingOf(Follow following) {
        User followingUser = following.getFollowing();
        return FollowVo.builder()
                .nickName(followingUser.getUsername())
                .monsterCode(followingUser.getMonsterCode())
                .monsterId(followingUser.getMonster().getMonsterDatabase().getId())
                .isFollowed(true)
                .monsterImg(followingUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    public static FollowVo followingOf(Follow following, Boolean checkFollow) {
        User followingUser = following.getFollowing();
        return FollowVo.builder()
                .nickName(followingUser.getUsername())
                .monsterCode(followingUser.getMonsterCode())
                .monsterId(followingUser.getMonster().getMonsterDatabase().getId())
                .isFollowed(checkFollow)
                .monsterImg(followingUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }
}

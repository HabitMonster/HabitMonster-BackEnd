package com.sollertia.habit.domain.user.follow.dto;

import com.querydsl.core.annotations.QueryProjection;
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

    public static FollowVo followerOf(Follow follow, Boolean checkFollow) {
        User followerUser = follow.getFollower();
        return FollowVo.builder()
                .nickName(followerUser.getUsername())
                .monsterId(followerUser.getMonster().getMonsterDatabase().getId())
                .monsterCode(followerUser.getMonsterCode())
                .isFollowed(checkFollow)
                .monsterImg(followerUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    public static FollowVo followingOf(Follow follow) {
        User followingUser = follow.getFollowing();
        return FollowVo.builder()
                .nickName(followingUser.getUsername())
                .monsterCode(followingUser.getMonsterCode())
                .monsterId(followingUser.getMonster().getMonsterDatabase().getId())
                .isFollowed(true)
                .monsterImg(followingUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    public static FollowVo followingOf(Follow follow, Boolean checkFollow) {
        User followingUser = follow.getFollowing();
        return FollowVo.builder()
                .nickName(followingUser.getUsername())
                .monsterCode(followingUser.getMonsterCode())
                .monsterId(followingUser.getMonster().getMonsterDatabase().getId())
                .isFollowed(checkFollow)
                .monsterImg(followingUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    @QueryProjection
    public FollowVo(String nickName, Long monsterId, String monsterImg, String monsterCode, Boolean isFollowed) {
        this.nickName = nickName;
        this.monsterId = monsterId;
        this.monsterImg = monsterImg;
        this.monsterCode = monsterCode;
        this.isFollowed = isFollowed;
    }
}

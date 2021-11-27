package com.sollertia.habit.domain.user.follow.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FollowDto {

    private String nickName;
    private Long monsterId;
    private String monsterImg;
    private String monsterCode;
    private Boolean isFollowed;

    @QueryProjection
    public FollowDto(String nickName, Long monsterId, String monsterImg, String monsterCode, Boolean isFollowed) {
        this.nickName = nickName;
        this.monsterId = monsterId;
        this.monsterImg = monsterImg;
        this.monsterCode = monsterCode;
        this.isFollowed = isFollowed;
    }
}

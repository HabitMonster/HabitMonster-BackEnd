package com.sollertia.habit.domain.user.follow.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sollertia.habit.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class FollowSearchResponseVo {
    private String nickName;
    private Long monsterId;
    private String monsterImg;
    private String monsterCode;
    private Boolean isFollowed;

    public static FollowSearchResponseVo
    of(User searchUser, Boolean checkFollow){
        return FollowSearchResponseVo.builder()
                .nickName(searchUser.getUsername())
                .monsterId(searchUser.getMonster().getMonsterDatabase().getId())
                .monsterCode(searchUser.getMonsterCode())
                .isFollowed(checkFollow)
                .monsterImg(searchUser.getMonster().getMonsterDatabase().getImageUrl())
                .build();
    }

    @QueryProjection
    public FollowSearchResponseVo(String nickName, Long monsterId, String monsterImg, String monsterCode, Boolean isFollowed) {
        this.nickName = nickName;
        this.monsterId = monsterId;
        this.monsterImg = monsterImg;
        this.monsterCode = monsterCode;
        this.isFollowed = isFollowed;
    }
}

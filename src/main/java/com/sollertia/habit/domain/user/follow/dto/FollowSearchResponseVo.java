package com.sollertia.habit.domain.user.follow.dto;

import com.sollertia.habit.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
}

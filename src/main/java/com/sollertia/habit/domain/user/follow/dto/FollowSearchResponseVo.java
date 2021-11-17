package com.sollertia.habit.domain.user.follow.dto;

import com.sollertia.habit.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowSearchResponseVo {
    private String email;
    private String monsterName;
    private String monsterImg;
    private String monsterCode;
    private Boolean isFollowed;

    public static  FollowSearchResponseVo of(User searchUser, Boolean checkFollow){
        return FollowSearchResponseVo.builder().email(searchUser.getEmail() != null ? searchUser.getEmail() : searchUser.getUsername())
                .monsterName(searchUser.getMonster().getName()).monsterCode(searchUser.getSocialId()).isFollowed(checkFollow)
                .monsterImg(searchUser.getMonster().getMonsterDatabase().getImageUrl()).build();
    }
}
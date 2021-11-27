package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.ProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoVo {
    private String monsterCode;
    private String username;
    private String monsterName;

    public static UserInfoVo of(User user) {
        if ( user.getMonster() == null ) {
            return UserInfoVo.builder()
                    .monsterCode(user.getMonsterCode())
                    .username(user.getUsername())
                    .monsterName(null)
                    .build();
        } else {
            return UserInfoVo.builder()
                    .monsterCode(user.getMonsterCode())
                    .username(user.getUsername())
                    .monsterName(user.getMonster().getName())
                    .build();
        }
    }
}
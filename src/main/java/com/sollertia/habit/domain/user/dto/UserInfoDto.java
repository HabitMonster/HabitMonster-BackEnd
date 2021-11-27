package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String monsterCode;
    private String username;
    private String monsterName;

    public static UserInfoDto of(User user) {
        if ( user.getMonster() == null ) {
            return UserInfoDto.builder()
                    .monsterCode(user.getMonsterCode())
                    .username(user.getUsername())
                    .monsterName(null)
                    .build();
        } else {
            return UserInfoDto.builder()
                    .monsterCode(user.getMonsterCode())
                    .username(user.getUsername())
                    .monsterName(user.getMonster().getName())
                    .build();
        }
    }
}

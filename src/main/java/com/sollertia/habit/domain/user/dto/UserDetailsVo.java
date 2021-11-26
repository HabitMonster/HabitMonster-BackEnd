package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailsVo {
    private String monsterCode;
    private String username;
    private String email;
    private Boolean isFollowed;
    private Integer totalHabitCount;
    private Long followersCount;
    private Long followingsCount;

    public static UserDetailsVo from(UserMonsterVo userMonsterVo, FollowCount followCount, Integer totalHabitCount) {
        return UserDetailsVo.builder()
                .monsterCode(userMonsterVo.getMonsterCode())
                .username(userMonsterVo.getUsername())
                .email(userMonsterVo.getEmail())
                .isFollowed(userMonsterVo.getIsFollowed())
                .totalHabitCount(totalHabitCount)
                .followersCount(followCount.getFollowersCount())
                .followingsCount(followCount.getFollowingsCount())
                .build();
    }
}

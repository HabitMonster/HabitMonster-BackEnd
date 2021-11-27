package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailsDto {
    private String monsterCode;
    private String username;
    private String email;
    private Boolean isFollowed;
    private Integer totalHabitCount;
    private Long followersCount;
    private Long followingsCount;

    public static UserDetailsDto from(UserMonsterDto userMonsterDto, FollowCount followCount, Integer totalHabitCount) {
        return UserDetailsDto.builder()
                .monsterCode(userMonsterDto.getMonsterCode())
                .username(userMonsterDto.getUsername())
                .email(userMonsterDto.getEmail())
                .isFollowed(userMonsterDto.getIsFollowed())
                .totalHabitCount(totalHabitCount)
                .followersCount(followCount.getFollowersCount())
                .followingsCount(followCount.getFollowingsCount())
                .build();
    }
}

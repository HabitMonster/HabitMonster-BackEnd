package com.sollertia.habit.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailsVo {
    private String monsterCode;
    private String username;
    private String email;
    private boolean isFollowed;
    private Integer followsCount;
    private Integer followingsCount;
}

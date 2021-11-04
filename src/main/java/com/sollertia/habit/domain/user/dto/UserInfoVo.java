package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoVo {
    private String socialId;
    private String username;
    private String email;
    private Long expPercentage;
    private Integer level;
    private UserType socialType;
}

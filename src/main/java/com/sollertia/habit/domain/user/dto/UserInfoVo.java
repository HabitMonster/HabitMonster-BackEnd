package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.ProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoVo {
    private String monsterCode;
    private String username;
    private String email;
    private ProviderType socialType;
}

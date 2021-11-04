package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.ProviderType;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserInfoResponseDto extends DefaultResponseDto {

    private String socialId;
    private String username;
    private String email;
    private Long expPercentage;
    private Integer level;
    private ProviderType providerType;
}

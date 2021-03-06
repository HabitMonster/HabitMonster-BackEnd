package com.sollertia.habit.domain.user.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserInfoResponseDto extends DefaultResponseDto {
    private UserInfoDto userInfo;
}

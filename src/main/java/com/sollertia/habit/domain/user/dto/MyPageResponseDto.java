package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.monster.dto.MonsterDto;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MyPageResponseDto extends DefaultResponseDto {
    UserDetailsDto userInfo;
    MonsterDto monster;
}
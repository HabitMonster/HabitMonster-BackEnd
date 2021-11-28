package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.habit.dto.HabitSummaryDto;
import com.sollertia.habit.domain.monster.dto.MonsterDto;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class UserDetailResponseDto extends DefaultResponseDto {
    UserDetailsDto userInfo;
    List<HabitSummaryDto> habits;
    MonsterDto monster;
}

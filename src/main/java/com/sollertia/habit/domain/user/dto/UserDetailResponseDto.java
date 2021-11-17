package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class UserDetailResponseDto extends DefaultResponseDto {
    UserDetailsVo userInfo;
    List<HabitSummaryVo> habits;
    MonsterVo monster;
}

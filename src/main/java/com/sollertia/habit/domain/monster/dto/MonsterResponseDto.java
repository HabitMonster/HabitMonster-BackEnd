package com.sollertia.habit.domain.monster.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class MonsterResponseDto extends DefaultResponseDto {
    private MonsterVo monster;
}

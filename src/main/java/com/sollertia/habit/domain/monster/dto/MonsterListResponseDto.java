package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class MonsterListResponseDto extends DefaultResponseDto {
    private List<Monster> monsters;
}

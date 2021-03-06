package com.sollertia.habit.domain.monster.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class MonsterListResponseDto extends DefaultResponseDto {
    private List<MonsterSummaryDto> monsters;
}

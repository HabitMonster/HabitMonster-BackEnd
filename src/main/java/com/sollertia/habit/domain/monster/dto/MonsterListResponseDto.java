package com.sollertia.habit.domain.monster.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class MonsterListResponseDto extends DefaultResponseDto {
    private List<MonsterSummaryVo> monsters;
}

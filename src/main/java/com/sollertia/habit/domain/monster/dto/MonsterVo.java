package com.sollertia.habit.domain.monster.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MonsterVo {
    private Long monsterid;
    private String monsterImage;
    private String monsterName;
}

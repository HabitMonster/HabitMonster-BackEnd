package com.sollertia.habit.domain.monster.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MonsterSelectRequestDto {
   private Long monsterId;
   private String monsterName;
}

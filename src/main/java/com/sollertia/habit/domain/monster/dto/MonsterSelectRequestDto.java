package com.sollertia.habit.domain.monster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonsterSelectRequestDto {
   private Long monsterId;
   private String monsterName;
}

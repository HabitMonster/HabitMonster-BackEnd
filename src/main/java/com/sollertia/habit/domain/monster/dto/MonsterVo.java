package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.Monster;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonsterVo {
    private String monsterImage;
    private String monsterName;
    private Integer monsterLevel;
    private Long monsterExpPoint;

    public static MonsterVo of(Monster monster) {
        return MonsterVo.builder()
                .monsterImage(monster.getMonsterDatabase().getImageUrl())
                .monsterName(monster.getName())
                .monsterLevel(monster.getLevel().getValue())
                .monsterExpPoint(monster.getExpPoint())
                .build();
    }
}

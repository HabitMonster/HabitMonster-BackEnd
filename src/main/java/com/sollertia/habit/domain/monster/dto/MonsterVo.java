package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.Monster;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonsterVo {
    private Long monsterId;
    private String monsterImage;
    private String monsterName;

    public static MonsterVo of(Monster monster, String name) {
        return MonsterVo.builder()
                .monsterId(monster.getId())
                .monsterImage(monster.getImageUrl())
                .monsterName(name)
                .build();
    }
}

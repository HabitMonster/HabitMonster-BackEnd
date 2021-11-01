package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.Monster;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonsterVo {
    private Long monsterid;
    private String monsterImage;
    private String monsterName;

    public static MonsterVo of(Monster monster, String name) {
        return MonsterVo.builder()
                .monsterid(monster.getId())
                .monsterImage(monster.getImageUrl())
                .monsterName(name)
                .build();
    }
}

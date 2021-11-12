package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MonsterVo {
    private String monsterImage;
    private String monsterName;
    private Integer monsterLevel;
    private Long monsterExpPoint;
    private String createAt;

    public static MonsterVo of(Monster monster) {
        return MonsterVo.builder()
                .monsterImage(monster.getMonsterDatabase().getImageUrl())
                .monsterName(monster.getName())
                .monsterLevel(monster.getLevel().getValue())
                .monsterExpPoint(monster.getExpPoint())
                .createAt(monster.getCreatedAt().toLocalDate().toString())
                .build();
    }
}

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
                .createAt(monster.getCreateAt().toString())
                .build();
    }

    public static MonsterVo of(MonsterCollection monsterCollection) {
        return MonsterVo.builder()
                .monsterImage(monsterCollection.getMonsterDatabase().getImageUrl())
                .monsterName(monsterCollection.getMonsterName())
                .monsterLevel(monsterCollection.getLevel().getValue())
                .createAt(monsterCollection.getCreateAt())
                .build();
    }

    public static List<MonsterVo> listOf(List<MonsterCollection> monsterCollections) {
        List<MonsterVo> monsterVoList = new ArrayList<>();
        for (MonsterCollection monsterCollection : monsterCollections) {
            monsterVoList.add(MonsterVo.of(monsterCollection));
        }
        return monsterVoList;
    }
}

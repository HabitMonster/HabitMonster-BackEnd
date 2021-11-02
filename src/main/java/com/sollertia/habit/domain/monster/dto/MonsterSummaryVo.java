package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.domain.monster.MonsterCollection;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MonsterSummaryVo {
    private Long monsterid;
    private String monsterImage;

    public static MonsterSummaryVo of(Monster monster) {
        return MonsterSummaryVo.builder()
                .monsterid(monster.getId())
                .monsterImage(monster.getImageUrl())
                .build();
    }

    public static List<MonsterSummaryVo> listFromMonsterList(List<Monster> monsterList) {
        List<MonsterSummaryVo> summaryVoList = new ArrayList<>();
        for (Monster monster : monsterList) {
            summaryVoList.add(MonsterSummaryVo.of(monster));
        }
        return summaryVoList;
    }

    public static List<MonsterSummaryVo> listFromCollectionList(List<MonsterCollection> monsterCollectionList) {
        List<MonsterSummaryVo> summaryVoList = new ArrayList<>();
        for (MonsterCollection monsterCollection : monsterCollectionList) {
            summaryVoList.add(MonsterSummaryVo.of(monsterCollection.getMonster()));
        }
        return summaryVoList;
    }
}

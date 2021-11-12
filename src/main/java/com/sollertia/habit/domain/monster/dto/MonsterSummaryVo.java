package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class MonsterSummaryVo {
    private Long monsterId;
    private String monsterImage;

    public static MonsterSummaryVo of(MonsterDatabase monsterDatabase) {
        return MonsterSummaryVo.builder()
                .monsterId(monsterDatabase.getId())
                .monsterImage(monsterDatabase.getImageUrl())
                .build();
    }

    public static List<MonsterSummaryVo> listFromMonsterList(List<MonsterDatabase> monsterList) {
        List<MonsterSummaryVo> summaryVoList = new ArrayList<>();
        for (MonsterDatabase monsterDatabase : monsterList) {
            summaryVoList.add(MonsterSummaryVo.of(monsterDatabase));
        }
        return summaryVoList;
    }
}

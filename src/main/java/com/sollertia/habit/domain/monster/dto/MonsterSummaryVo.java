package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class MonsterSummaryVo {
    private Long monsterId;
    private String monsterImage;
    private boolean enable = true;

    public MonsterSummaryVo(Long monsterId, String monsterImage) {
        this.monsterId = monsterId;
        this.monsterImage = monsterImage;
    }

    private void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void toDisable() {
        this.setEnable(false);
    }

    public static MonsterSummaryVo of(MonsterDatabase monsterDatabase) {
        return new MonsterSummaryVo(monsterDatabase.getId(), monsterDatabase.getImageUrl());
    }

    public static MonsterSummaryVo disabledOf(MonsterDatabase monsterDatabase) {
        MonsterSummaryVo summaryVo = new MonsterSummaryVo(monsterDatabase.getId(), monsterDatabase.getImageUrl());
        summaryVo.toDisable();
        return summaryVo;
    }

    public static List<MonsterSummaryVo> listFromMonsterDatabasesDisabledIfNotIn(List<MonsterDatabase> monsterList, List<MonsterType> monsterTypeList) {
        List<MonsterSummaryVo> summaryVoList = new ArrayList<>();
        for (MonsterDatabase monsterDatabase : monsterList) {
            if ( monsterTypeList.contains(monsterDatabase.getMonsterType()) ) {
                summaryVoList.add(MonsterSummaryVo.disabledOf(monsterDatabase));
            } else {
                summaryVoList.add(MonsterSummaryVo.of(monsterDatabase));
            }
        }
        return summaryVoList;
    }
}

package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonsterSummaryDto {
    private Long monsterId;
    private String monsterImage;
    private boolean enable = true;

    public MonsterSummaryDto(Long monsterId, String monsterImage) {
        this.monsterId = monsterId;
        this.monsterImage = monsterImage;
    }

    private void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void toDisable() {
        this.setEnable(false);
    }

    public static MonsterSummaryDto of(MonsterDatabase monsterDatabase) {
        return new MonsterSummaryDto(monsterDatabase.getId(), monsterDatabase.getImageUrl());
    }

    public static MonsterSummaryDto disabledOf(MonsterDatabase monsterDatabase) {
        MonsterSummaryDto summaryVo = new MonsterSummaryDto(monsterDatabase.getId(), monsterDatabase.getImageUrl());
        summaryVo.toDisable();
        return summaryVo;
    }

    public static List<MonsterSummaryDto> listFromMonsterDatabasesDisabledIfNotIn(List<MonsterDatabase> monsterList, List<MonsterType> monsterTypeList) {
        List<MonsterSummaryDto> summaryVoList = new ArrayList<>();
        for (MonsterDatabase monsterDatabase : monsterList) {
            if ( monsterTypeList.contains(monsterDatabase.getMonsterType()) ) {
                summaryVoList.add(MonsterSummaryDto.disabledOf(monsterDatabase));
            } else {
                summaryVoList.add(MonsterSummaryDto.of(monsterDatabase));
            }
        }
        return summaryVoList;
    }
}

package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonsterCollectionVo {
    private String monsterName;
    private String createdAt;
    private Integer maxLevel;
    private List<MonsterDatabaseVo> monsterDatabases;

    private MonsterCollectionVo(String monsterName, String createdAt, Integer maxLevel, List<MonsterDatabaseVo> monsterDatabases) {
        this.monsterName = monsterName;
        this.createdAt = createdAt;
        this.maxLevel = maxLevel;
        this.monsterDatabases = monsterDatabases;
    }

    public static MonsterCollectionVo of(MonsterCollection monsterCollection, List<MonsterDatabaseVo> monsterDatabases) {
        return new MonsterCollectionVo(
                monsterCollection.getMonsterName(),
                monsterCollection.getCreateAt(),
                monsterCollection.getMaxLevel().getValue(),
                monsterDatabases
        );
    }

    public static List<MonsterCollectionVo> listOf(List<MonsterCollection> monsterCollectionList) {
        List<MonsterCollectionVo> monsterCollectionVoList = new ArrayList<>();

        for (MonsterCollection monsterCollection : monsterCollectionList) {
            List<MonsterCollectionDatabase> monsterCollectionDatabaseList =
                    monsterCollection.getMonsterCollectionDatabaseList();
            List<MonsterDatabaseVo> monsterDatabaseVoList = MonsterDatabaseVo.listOf(monsterCollectionDatabaseList);
            monsterCollectionVoList.add(MonsterCollectionVo.of(monsterCollection, monsterDatabaseVoList));
        }

        return monsterCollectionVoList;
    }
}

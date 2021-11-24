package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonsterDatabaseVo {
    private Long monsterId;
    private Integer monsterLevel;
    private String monsterImage;

    private MonsterDatabaseVo(Long monsterId, Integer monsterLevel, String monsterImage) {
        this.monsterId = monsterId;
        this.monsterLevel = monsterLevel;
        this.monsterImage = monsterImage;
    }

    public static MonsterDatabaseVo of(MonsterDatabase monsterDatabase) {
        return new MonsterDatabaseVo(
                monsterDatabase.getId(),
                monsterDatabase.getLevel().getValue(),
                monsterDatabase.getImageUrl());
    }

    public static List<MonsterDatabaseVo> listOf(List<MonsterCollectionDatabase> monsterCollectionDatabaseList) {
        List<MonsterDatabaseVo> monsterDatabaseVoList = new ArrayList<>();
        for (MonsterCollectionDatabase monsterCollectionDatabase : monsterCollectionDatabaseList) {
            monsterDatabaseVoList.add(MonsterDatabaseVo.of(monsterCollectionDatabase.getMonsterDatabase()));
        }
        return monsterDatabaseVoList;
    }
}

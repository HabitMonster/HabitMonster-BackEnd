package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public MonsterCollectionVo of(MonsterCollection monsterCollection, List<MonsterDatabaseVo> monsterDatabases) {
        return new MonsterCollectionVo(
                monsterCollection.getMonsterName(),
                monsterCollection.getCreateAt(),
                monsterCollection.getMaxLevel().getValue(),
                monsterDatabases
        );
    }
}

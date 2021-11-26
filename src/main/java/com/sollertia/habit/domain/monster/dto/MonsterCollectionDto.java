package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonsterCollectionDto {
    private String monsterName;
    private String createdAt;
    private Integer maxLevel;
    private List<MonsterDatabaseDto> monsterDatabases;

    private MonsterCollectionDto(String monsterName, String createdAt, Integer maxLevel, List<MonsterDatabaseDto> monsterDatabases) {
        this.monsterName = monsterName;
        this.createdAt = createdAt;
        this.maxLevel = maxLevel;
        this.monsterDatabases = monsterDatabases;
    }

    public static MonsterCollectionDto of(MonsterCollection monsterCollection, List<MonsterDatabaseDto> monsterDatabases) {
        return new MonsterCollectionDto(
                monsterCollection.getMonsterName(),
                monsterCollection.getCreateAt(),
                monsterCollection.getMaxLevel().getValue(),
                monsterDatabases
        );
    }

    public static List<MonsterCollectionDto> listOf(List<MonsterCollection> monsterCollectionList) {
        List<MonsterCollectionDto> monsterCollectionDtoList = new ArrayList<>();

        for (MonsterCollection monsterCollection : monsterCollectionList) {
            List<MonsterCollectionDatabase> monsterCollectionDatabaseList =
                    monsterCollection.getMonsterCollectionDatabaseList();
            List<MonsterDatabaseDto> monsterDatabaseDtoList = MonsterDatabaseDto.listOf(monsterCollectionDatabaseList);
            monsterCollectionDtoList.add(MonsterCollectionDto.of(monsterCollection, monsterDatabaseDtoList));
        }

        return monsterCollectionDtoList;
    }
}

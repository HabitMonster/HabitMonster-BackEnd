package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonsterDatabaseDto {
    private Long monsterId;
    private Integer monsterLevel;
    private String monsterImage;

    private MonsterDatabaseDto(Long monsterId, Integer monsterLevel, String monsterImage) {
        this.monsterId = monsterId;
        this.monsterLevel = monsterLevel;
        this.monsterImage = monsterImage;
    }

    public static MonsterDatabaseDto of(MonsterDatabase monsterDatabase) {
        return new MonsterDatabaseDto(
                monsterDatabase.getId(),
                monsterDatabase.getLevel().getValue(),
                monsterDatabase.getImageUrl());
    }

    public static List<MonsterDatabaseDto> listOf(List<MonsterCollectionDatabase> monsterCollectionDatabaseList) {
        List<MonsterDatabaseDto> monsterDatabaseDtoList = new ArrayList<>();
        for (MonsterCollectionDatabase monsterCollectionDatabase : monsterCollectionDatabaseList) {
            monsterDatabaseDtoList.add(MonsterDatabaseDto.of(monsterCollectionDatabase.getMonsterDatabase()));
        }
        return monsterDatabaseDtoList;
    }
}

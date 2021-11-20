package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import lombok.Getter;

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
}

package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import lombok.Getter;

@Getter
public class MonsterDatabaseVo {
    private Integer monsterLevel;
    private String monsterImage;

    private MonsterDatabaseVo(Integer monsterLevel, String monsterImage) {
        this.monsterLevel = monsterLevel;
        this.monsterImage = monsterImage;
    }

    public static MonsterDatabaseVo of(MonsterDatabase monsterDatabase) {
        return new MonsterDatabaseVo(
                monsterDatabase.getLevel().getValue(),
                monsterDatabase.getImageUrl());
    }
}

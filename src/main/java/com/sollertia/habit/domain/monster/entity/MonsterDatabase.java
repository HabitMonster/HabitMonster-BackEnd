package com.sollertia.habit.domain.monster.entity;

import com.sollertia.habit.domain.monster.enums.Level;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MonsterDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private MonsterType monsterType;

    protected MonsterDatabase() {
    }

    public MonsterDatabase(Level level, MonsterType monsterType, String imageUrl) {
        this.level = level;
        this.monsterType = monsterType;
        this.imageUrl = imageUrl;
    }
}

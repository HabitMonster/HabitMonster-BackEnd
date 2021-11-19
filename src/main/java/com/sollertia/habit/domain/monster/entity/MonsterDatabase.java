package com.sollertia.habit.domain.monster.entity;

import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonsterDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Level level;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private MonsterType monsterType;

    public MonsterDatabase(Level level, MonsterType monsterType, String imageUrl) {
        this.level = level;
        this.monsterType = monsterType;
        this.imageUrl = imageUrl;
    }
}

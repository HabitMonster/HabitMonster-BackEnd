package com.sollertia.habit.domain.monster.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MonsterCollectionDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_database_id")
    private MonsterDatabase monsterDatabase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_collection_id")
    private MonsterCollection monsterCollection;

    private MonsterCollectionDatabase(MonsterDatabase monsterDatabase, MonsterCollection monsterCollection) {
        this.monsterDatabase = monsterDatabase;
        this.monsterCollection = monsterCollection;
    }

    public static MonsterCollectionDatabase from(MonsterDatabase monsterDatabase, MonsterCollection monsterCollection) {
        MonsterCollectionDatabase monsterCollectionDatabase = new MonsterCollectionDatabase(monsterDatabase, monsterCollection);
        monsterCollection.addMonsterCollectionDatabase(monsterCollectionDatabase);
        return monsterCollectionDatabase;
    }

    protected MonsterCollectionDatabase() {}
}

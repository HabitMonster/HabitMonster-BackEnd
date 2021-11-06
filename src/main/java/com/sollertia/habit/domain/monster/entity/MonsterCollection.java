package com.sollertia.habit.domain.monster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MonsterCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_database_id")
    @JsonIgnore
    private MonsterDatabase monsterDatabase;

    private String monsterName;

    protected MonsterCollection() {
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setMonster(MonsterDatabase monsterDatabase) {
        this.monsterDatabase = monsterDatabase;
    }

    private void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public static MonsterCollection createMonsterCollection(Monster monster) {
        MonsterCollection monsterCollection = new MonsterCollection();
        monsterCollection.setMonster(monster.getMonsterDatabase());
        monsterCollection.setUser(monster.getUser());
        monsterCollection.setMonsterName(monster.getName());
        return monsterCollection;
    }
}

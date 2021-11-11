package com.sollertia.habit.domain.monster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.Level;
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

    private Level level;

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

    public void setLevel(Level level) {
        this.level = level;
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
        monsterCollection.setLevel(monster.getLevel());
        monsterCollection.setMonsterName(monster.getName());
        return monsterCollection;
    }
}

package com.sollertia.habit.domain.monster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.monster.enums.Level;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Level maxLevel;

    @Enumerated(EnumType.STRING)
    private MonsterType monsterType;

    @OneToMany(mappedBy = "monsterCollection")
    @JsonIgnore
    private List<MonsterCollectionDatabase> monsterCollectionDatabaseList = new ArrayList<>();

    private String monsterName;

    private String createAt;

    protected MonsterCollection() {
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setMaxLevel(Level maxLevel) {
        this.maxLevel = maxLevel;
    }

    private void setCreateAt(String createAt){this.createAt=createAt;}

    public void addMonsterCollectionDatabase(MonsterCollectionDatabase monsterCollectionDatabase) {
        this.monsterCollectionDatabaseList.add(monsterCollectionDatabase);
    }

    private void setMonsterType(MonsterType monsterType) {
        this.monsterType = monsterType;
    }

    private void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public static MonsterCollection createMonsterCollection(Monster monster) {
        MonsterCollection monsterCollection = new MonsterCollection();
        monsterCollection.setUser(monster.getUser());
        monsterCollection.setMaxLevel(monster.getLevel());
        monsterCollection.setMonsterType(monster.getMonsterDatabase().getMonsterType());
        monsterCollection.setMonsterName(monster.getName());
        monsterCollection.setCreateAt(monster.getCreatedAt().toLocalDate().toString());
        return monsterCollection;
    }
}

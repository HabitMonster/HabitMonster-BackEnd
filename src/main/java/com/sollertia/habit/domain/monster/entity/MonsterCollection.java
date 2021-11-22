package com.sollertia.habit.domain.monster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "monsterCollection", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MonsterCollectionDatabase> monsterCollectionDatabaseList = new ArrayList<>();

    private String monsterName;

    private String createAt;

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

    public void updateMaxLevel(Level level) {
        setMaxLevel(level);
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

    public void updateMonsterName(String monsterName) {
        this.setMonsterName(monsterName);
    }
}

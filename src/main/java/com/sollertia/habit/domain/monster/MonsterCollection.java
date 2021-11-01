package com.sollertia.habit.domain.monster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MonsterCollection {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_id")
    @JsonIgnore
    private Monster monster;

    private String monsterName;

    protected MonsterCollection() {
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setMonster(Monster monster) {
        this.monster = monster;
    }

    private void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public static MonsterCollection createMonsterCollection(User user, Monster monster) {
        MonsterCollection monsterCollection = new MonsterCollection();
        monsterCollection.setMonster(monster);
        monsterCollection.setUser(user);
        monsterCollection.setMonsterName(user.getMonsterName());
        return monsterCollection;
    }
}

package com.sollertia.habit.domain.monster.entity;

import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Monster extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long expPoint;

    @Enumerated(value = EnumType.STRING)
    private Level level;

    @ManyToOne(fetch = FetchType.LAZY)
    private MonsterDatabase monsterDatabase;

    @OneToOne(mappedBy = "monster", fetch = FetchType.LAZY)
    @Setter
    private User user;

    private void setLevel(Level level){this.level = level;}

    private void setExpPoint(Long expPoint){this.expPoint = expPoint;}

    private void setName(String name) {
        this.name = name;
    }

    private void setMonsterDatabase(MonsterDatabase monsterDatabase) {
        this.monsterDatabase = monsterDatabase;
    }

    public boolean plusExpPoint() {
        setExpPoint(getExpPoint() + getLevel().getPlusPoint());
        if ( getExpPoint() >= Level.MAX_EXP ) {
            if ( getLevel().isMax() ) {
                setExpPoint(Level.MAX_EXP);
                return false;
            } else {
                setExpPoint(getExpPoint() - Level.MAX_EXP);
                return true;
            }
        }
        return false;
    }

    public void minusExpPoint() {
        setExpPoint(getExpPoint() - getLevel().getMinusPoint());
        if (getExpPoint() < Level.MIN_EXP) {
            setExpPoint(Level.MIN_EXP);
        }
    }

    public Level levelUp() {
        this.setLevel(Level.nextOf(this.getLevel()));
        return this.getLevel();
    }

    public static Monster createNewMonster(String monsterName, MonsterDatabase monsterDatabase) {
        Monster newMonster =  new Monster();
        newMonster.setLevel(Level.LV1);
        newMonster.setExpPoint(0L);
        newMonster.setName(monsterName);
        newMonster.setMonsterDatabase(monsterDatabase);
        return newMonster;
    }

    public Monster updateName(String name){
        setName(name);
        return this;
    }

    public void updateMonsterDatabase(MonsterDatabase monsterDatabase) {
        setMonsterDatabase(monsterDatabase);
    }

    public boolean changeable() {
        return this.getLevel().getValue().equals(Level.MAX_LEVEL) &
                this.getExpPoint().equals(Level.MAX_EXP);
    }
}

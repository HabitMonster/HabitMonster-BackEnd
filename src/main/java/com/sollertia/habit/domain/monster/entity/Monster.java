package com.sollertia.habit.domain.monster.entity;

import com.sollertia.habit.domain.user.enums.Level;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
public class Monster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long expPoint;

    @Enumerated(value = EnumType.STRING)
    private Level level;

    @ManyToOne
    private MonsterDatabase monsterDatabase;

    @OneToOne(mappedBy = "monster", fetch = FetchType.LAZY)
    @Setter
    private User user;

    private LocalDate createAt;

    protected Monster() {
    }

    private void setLevel(Level level){this.level = level;}

    private void setExpPoint(Long expPoint){this.expPoint = expPoint;}

    private void setName(String name) {
        this.name = name;
    }

    private void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    private void setMonsterDatabase(MonsterDatabase monsterDatabase) {
        this.monsterDatabase = monsterDatabase;
    }

    public void plusExpPoint() {
        setExpPoint(getExpPoint() + this.level.getPlusPoint());
        long margin = getExpPoint() - Level.MAX_EXP;
        if ( margin >= 0 ) {
            levelUp();
            this.expPoint = margin;
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
        newMonster.setCreateAt(LocalDate.now());
        return newMonster;
    }

    public Monster updateName(String name){
        setName(name);
        return this;
    }

}

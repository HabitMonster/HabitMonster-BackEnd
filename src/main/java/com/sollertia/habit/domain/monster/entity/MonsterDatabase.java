package com.sollertia.habit.domain.monster.entity;

import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MonsterDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EvolutionGrade grade;

    private String imageUrl;

    protected MonsterDatabase() {
    }

    public MonsterDatabase(EvolutionGrade grade, String imageUrl) {
        this.grade = grade;
        this.imageUrl = imageUrl;
    }
}

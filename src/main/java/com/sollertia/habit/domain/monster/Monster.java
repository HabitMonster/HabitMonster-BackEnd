package com.sollertia.habit.domain.monster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor //test
@AllArgsConstructor //test
public class Monster {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EvolutionGrade grade;

    private String imageUrl;

    public Monster(EvolutionGrade grade, String imageUrl) {
        this.grade = grade;
        this.imageUrl = imageUrl;
    }
}

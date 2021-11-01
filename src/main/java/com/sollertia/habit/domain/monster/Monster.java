package com.sollertia.habit.domain.monster;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Monster {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    @Enumerated(EnumType.STRING)
    private EvolutionGrade grade;

    private String imageUrl;

}

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

    private String type;

    @Enumerated(EnumType.STRING)
    private EvolutionGrade grade;

    private String imageUrl;

}

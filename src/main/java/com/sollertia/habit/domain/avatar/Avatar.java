package com.sollertia.habit.domain.avatar;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    @Enumerated(EnumType.STRING)
    private EvolutionGrade grade;

    private String imageUrl;

}

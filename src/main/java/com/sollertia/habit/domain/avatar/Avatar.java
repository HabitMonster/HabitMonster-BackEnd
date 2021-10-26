package com.sollertia.habit.domain.avatar;

import javax.persistence.*;

@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    @Enumerated(EnumType.STRING)
    private EvolutionGrade grade;

    private String imageUrl;

}

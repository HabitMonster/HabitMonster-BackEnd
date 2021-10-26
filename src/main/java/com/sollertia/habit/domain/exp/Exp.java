package com.sollertia.habit.domain.exp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Exp {
    @Id
    @GeneratedValue
    private Long id;

    long points;

    // Long currentRatio;
}

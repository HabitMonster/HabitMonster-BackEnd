package com.sollertia.habit.domain.exp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExpPerLevel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private int level;

    private int exp;
}

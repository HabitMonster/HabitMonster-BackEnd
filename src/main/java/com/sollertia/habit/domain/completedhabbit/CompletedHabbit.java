package com.sollertia.habit.domain.completedhabbit;

import com.sollertia.habit.domain.habbit.enums.Category;
import com.sollertia.habit.domain.habbit.enums.HabbitType;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;

@Entity
public class CompletedHabbit {

    @Id
    @GeneratedValue
    Long id;

    int accomplishedSessionCounter;

    @ManyToOne
    User user;

    long goalPercentage;

    @Enumerated(EnumType.STRING)
    Category category;

    @Enumerated(EnumType.STRING)
    HabbitType habbitType;

    @Column(nullable = true)
    Long GoalTime;

    @Column(nullable = true)
    Long goalCount;

    Long experiencePoint;
}

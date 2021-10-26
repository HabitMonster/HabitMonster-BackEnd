package com.sollertia.habit.domain.completedhabbit;

import com.sollertia.habit.domain.habbit.enums.Category;
import com.sollertia.habit.domain.habbit.enums.HabbitType;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;

@Entity
public class CompletedHabbit {

    @Id
    @GeneratedValue
    private Long id;

    private int accomplishedSessionCounter;

    @ManyToOne
    private User user;

    private long goalPercentage;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private HabbitType habbitType;

    @Column(nullable = true)
    private Long GoalTime;

    @Column(nullable = true)
    private Long goalCount;

    private Long experiencePoint;
}

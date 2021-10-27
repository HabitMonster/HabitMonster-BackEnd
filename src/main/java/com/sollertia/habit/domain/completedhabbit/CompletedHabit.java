package com.sollertia.habit.domain.completedhabbit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;

@Entity
public class CompletedHabit {

    @Id
    @GeneratedValue
    private Long id;

    private int accomplishedSessionCounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    private long goalPercentage;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private HabitType habitType;

    @Column(nullable = true)
    private Long GoalTime;

    @Column(nullable = true)
    private Long goalCount;

    private Long experiencePoint;
}

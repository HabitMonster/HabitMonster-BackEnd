package com.sollertia.habit.domain.freeset;

import com.sollertia.habit.domain.habbit.enums.Category;
import com.sollertia.habit.domain.habbit.enums.Day;
import com.sollertia.habit.domain.habbit.enums.SessionDuration;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class FreeSet {
    @Id
    @GeneratedValue
    Long id;

    private String title;

    private String description;

    private Long experiencePoint;

    private Long currentDurationTime;

    private Long goalDurationTimePerSession;

    private Long goalCountPerSession;

    private LocalDateTime createdAt;

    private LocalDateTime endUpDateTime;

    @Enumerated(EnumType.STRING)
    private FreeSetType freeSetType;

    @Enumerated(EnumType.STRING)
    private SessionDuration sessionDuration;

    @Enumerated(EnumType.STRING)
    private Day day;

    @Enumerated(EnumType.STRING)
    private Category category;

}

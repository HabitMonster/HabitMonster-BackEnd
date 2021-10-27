package com.sollertia.habit.domain.habit;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@DiscriminatorValue("T")
public class HabitWithTimer extends Habit {

    // == case : 2 ==
    private Long currentDurationTime;
    private Long goalDurationTimePerSession;
}

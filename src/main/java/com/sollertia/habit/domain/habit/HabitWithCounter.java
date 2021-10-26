package com.sollertia.habit.domain.habit;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@DiscriminatorValue("C")
public class HabitWithCounter extends Habit {

    //현재 달성
    private Long count = 0l;
    //목표치
    private Long goalCountPerSession;

}
package com.sollertia.habit.domain.habbit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class HabbitWithCounter extends Habbit {

    //현재 달성
    private Long count = 0l;
    //목표치
    private Long goalCountPerSession;

}
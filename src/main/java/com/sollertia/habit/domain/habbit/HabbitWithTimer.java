package com.sollertia.habit.domain.habbit;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("T")
public class HabbitWithTimer extends Habbit {

    // == case : 1 ==
    //타이머 시작 호출마다 갱신
//    LocalDateTime startDateTime;
    //타이머 종료 호출마다 갱신
//    LocalDateTime dueDateTime;

    // == case : 2 ==
    private Long currentDurationTime;
    private Long goalDurationTimePerSession;
}

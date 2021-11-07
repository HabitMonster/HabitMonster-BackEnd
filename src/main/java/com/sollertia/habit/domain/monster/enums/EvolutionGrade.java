package com.sollertia.habit.domain.monster.enums;

public enum EvolutionGrade {
    EV1(1),EV2(2),EV3(3),EV4(4),EV5(5);

    private final Integer value;

    EvolutionGrade(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}

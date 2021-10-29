package com.sollertia.habit.domain.user;

import lombok.Getter;

@Getter
public enum Level {
    LV1(10, 3), Lv2(8, 4), LV3(6, 5), LV4(5, 6), LV5(0, 0);

    private final Integer plusPoint;
    private final Integer minusPoint;

    Level(Integer plusPoint, Integer minusPoint) {
        this.plusPoint = plusPoint;
        this.minusPoint = minusPoint;
    }
}

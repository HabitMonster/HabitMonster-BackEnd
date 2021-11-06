package com.sollertia.habit.domain.user.enums;

import lombok.Getter;

@Getter
public enum Level {
    LV1(1,10, 3),
    LV2(2,8, 4),
    LV3(3,6, 5),
    LV4(4,5, 6),
    LV5(5,0, 0);

    public static final int MAX_LEVEL = 5;

    private final Integer value;
    private final Integer plusPoint;
    private final Integer minusPoint;

    Level(Integer value, Integer plusPoint, Integer minusPoint) {
        this.value = value;
        this.plusPoint = plusPoint;
        this.minusPoint = minusPoint;
    }

    public static Level nextOf(Level level) {
        return Level.of(level.getValue() + 1);
    }

    private static Level of(Integer value) {
        if ( value == 1 ) {
            return LV1;
        } else if ( value == 2 ) {
            return LV2;
        } else if ( value == 3 ) {
            return LV3;
        } else if ( value == 4 ) {
            return LV4;
        } else if ( value == 5 ) {
            return LV5;
        } else {
            return null;
        }
    }
}

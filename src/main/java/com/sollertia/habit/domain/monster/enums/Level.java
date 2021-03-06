package com.sollertia.habit.domain.monster.enums;

import com.sollertia.habit.global.exception.monster.InvalidLevelException;
import lombok.Getter;

@Getter
public enum Level {
    LV1(1,10, 3),
    LV2(2,8, 4),
    LV3(3,6, 5),
    LV4(4,5, 6),
    LV5(5,4, 7);

    public static final Integer MAX_LEVEL = 5;
    public static final long MAX_EXP = 100;
    public static final long MIN_EXP = 0;

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
            throw new InvalidLevelException("불가능한 레벨입니다.");
        }
    }

    public boolean isMax() {
        return this.getValue() == Level.MAX_LEVEL;
    }
}

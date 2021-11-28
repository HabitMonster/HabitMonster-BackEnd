package com.sollertia.habit.domain.monster.enums;

import lombok.Getter;

@Getter
public enum MonsterType {
    GREEN(1L, "아보카도 눈사람"),
    PINK(6L, "도너츠 토끼"),
    BLUE(11L, "써니사이드업 소라"),
    YELLOW(16L, "바나나 코끼리"),
    ORANGE(21L, "레몬 마우스"),
    RED(26L, "피치 베어");

    private final Long lv1Id;
    private final String name;

    MonsterType(Long lv1Id, String name) {
        this.lv1Id = lv1Id;
        this.name = name;
    }
}

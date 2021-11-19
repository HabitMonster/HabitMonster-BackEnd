package com.sollertia.habit.domain.monster.enums;

public enum MonsterType {
    GREEN(1L), PINK(6L), BLUE(11L), YELLOW(16L), ORANGE(21L), RED(26L);

    private final Long lv1Id;

    MonsterType(Long lv1Id) {
        this.lv1Id = lv1Id;
    }

    public Long getLv1Id() {
        return lv1Id;
    }
}

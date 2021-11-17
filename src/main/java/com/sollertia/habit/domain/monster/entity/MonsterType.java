package com.sollertia.habit.domain.monster.entity;

public enum MonsterType {
    GREEN(1L), PINK(2L), BLUE(3L), YELLOW(4L), ORANGE(5L), RED(5L);

    private final Long lv1Id;

    MonsterType(Long lv1Id) {
        this.lv1Id = lv1Id;
    }

    public Long getLv1Id() {
        return lv1Id;
    }
}

package com.sollertia.habit.domain.preset.enums;

import java.util.Objects;

public enum Category {

    Health(1L), Study(2L), Life(3L),
    Emotion(4L), Relation(5L), Hobby(6L), Etc(7L);

    private final Long category;

    Category(Long category) {
        this.category = category;
    }

    public Long getType() {
        return this.category;
    }

    public static Category getCategory(Long category) {
        for ( Category c : Category.values()) {
            if (Objects.equals(c.category, category)) {
                return c;
            }
        }
        return null;
    }
}

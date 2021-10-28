package com.sollertia.habit.domain.habit.enums;

public enum HabitType {
    HABITWITHTIMER("timer"), HABITWITHCOUNTER("counter");

    private String type;

    HabitType(String type) {
        this.type = type;
    }

    public String getString() {
        return this.type;
    }

    public static HabitType fromString(String type) {
        for (HabitType t : HabitType.values()) {
            if (t.getString().equals(type)) {
                return t;
            }
        }
        return null;
    }
}



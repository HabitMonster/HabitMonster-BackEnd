package com.sollertia.habit.domain.habit.enums;

public enum HabitSession {
    NPERDAY("nPerDay"), SPECIFICDAY("specificDay");

    private String session;

    HabitSession(String session) {
        this.session = session;
    }

    public String getString() {
        return this.session;
    }

    public static HabitSession fromString(String session) {
        for (HabitSession h : HabitSession.values()) {
            if (h.getString().equals(session)) {
                return h;
            }
        }
        return null;
    }
}

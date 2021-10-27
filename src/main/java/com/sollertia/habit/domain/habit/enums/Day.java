package com.sollertia.habit.domain.habit.enums;

public enum Day {
    SUNDAY(1),MONDAY(2),TUESDAY(3),WEDNESDAY(4),TURSDAY(5),FRIDAY(6),SATURDAY(7);

    private Integer days;

    Day(int days) {
        this.days = days;
    }

    public int getInt() {
        return this.days;
    }

    public static Day fromInt(int days) {
        for (Day d : Day.values()) {
            if (d.days.equals(days)) {
                return d;
            }
        }
        return null;
    }
}

package com.sollertia.habit.domain.statistics.enums;

public enum SessionType {
    DAILY("어제"), WEEKLY("지난 주"), MONTHLY("지난 달"), STATIC("");

    private String parseString;

    SessionType(String parseString) {
        this.parseString = parseString;
    }

    public String getString() {
        return this.parseString;
    }
}

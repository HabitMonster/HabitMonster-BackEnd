package com.sollertia.habit.exception;

public class HabitTypeNotFoundException extends ClassCastException {
    public HabitTypeNotFoundException(String message) {
        super(message);
    }
}

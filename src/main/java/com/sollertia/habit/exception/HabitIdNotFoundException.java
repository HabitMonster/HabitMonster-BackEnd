package com.sollertia.habit.exception;

public class HabitIdNotFoundException extends RuntimeException {
    public HabitIdNotFoundException(String message) {
        super(message);
    }
}

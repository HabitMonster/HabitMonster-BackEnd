package com.sollertia.habit.global.exception.user;

public class HasNoPermissionException extends RuntimeException {
    public HasNoPermissionException(String message) {
        super(message);
    }
}

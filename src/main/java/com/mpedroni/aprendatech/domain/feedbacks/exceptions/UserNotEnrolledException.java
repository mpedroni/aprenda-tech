package com.mpedroni.aprendatech.domain.feedbacks.exceptions;

public class UserNotEnrolledException extends IllegalArgumentException {
    public UserNotEnrolledException(String courseCode) {
        super("User is not enrolled in course \"%s\".".formatted(courseCode));
    }
}

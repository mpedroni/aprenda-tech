package com.mpedroni.aprendatech.domain.enrollments.exceptions;

public class UserAlreadyEnrolledException extends IllegalArgumentException {
    public UserAlreadyEnrolledException(String username, String courseCode) {
        super(String.format("User %s is already enrolled in course \"%s\".", username, courseCode));
    }
}

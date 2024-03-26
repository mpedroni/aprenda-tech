package com.mpedroni.aprendatech.domain.enrollments.exceptions;

public class CourseInactiveException extends IllegalArgumentException {
    public CourseInactiveException(String courseCode) {
        super("Course \"%s\" is inactive".formatted(courseCode));
    }
}

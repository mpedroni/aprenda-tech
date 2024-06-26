package com.mpedroni.aprendatech.domain.courses.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String code) {
        super("Course with code \"" + code + "\" not found.");
    }

    public CourseNotFoundException(Long id) {
        super("Course with id " + id + " not found.");
    }
}

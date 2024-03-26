package com.mpedroni.aprendatech.domain.courses.exceptions;

public class CourseCodeAlreadyExistsException extends RuntimeException {
    public CourseCodeAlreadyExistsException(String code) {
        super("Course code \"" + code + "\" already exists");
    }
}

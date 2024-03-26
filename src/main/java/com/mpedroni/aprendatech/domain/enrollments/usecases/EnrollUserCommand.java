package com.mpedroni.aprendatech.domain.enrollments.usecases;

public record EnrollUserCommand(
        Long courseId,
        String username
) {
    static public EnrollUserCommand with(Long courseId, String username) {
        return new EnrollUserCommand(courseId, username);
    }
}

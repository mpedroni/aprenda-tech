package com.mpedroni.aprendatech.domain.courses.usecases;

public record CreateCourseCommand(
    String name,
    String code,
    Long instructorId,
    String description

) {
    static public CreateCourseCommand with(String name, String code, Long instructorId, String description) {
        return new CreateCourseCommand(name, code, instructorId, description);
    }
}

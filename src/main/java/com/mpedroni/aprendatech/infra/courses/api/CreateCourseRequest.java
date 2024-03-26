package com.mpedroni.aprendatech.infra.courses.api;

public record CreateCourseRequest(
    String name,
    String code,
    Long instructorId,
    String description
) {
}

package com.mpedroni.aprendatech.infra.courses.persistence;

public record CourseNpsMetrics(
        Long courseId,
        String code,
        Integer promoters,
        Integer detractors,
        Integer passives
) {
}

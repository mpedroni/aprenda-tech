package com.mpedroni.aprendatech.domain.reports.courses.usecases;

public record CourseNps(
        Long id,
        String code,
        Integer promoters,
        Integer detractors,
        Integer passives,
        Integer total,
        Integer nps
) {
}

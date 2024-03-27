package com.mpedroni.aprendatech.infra.courses.api;

import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;

import java.time.LocalDateTime;

public record SearchCourseResponse(
        Long id,
        String name,
        String code,
        String description,
        String status,
        LocalDateTime createdAt,
        LocalDateTime inactivatedAt,
        Long instructorId
) {
    static public SearchCourseResponse from(CourseJpaEntity course) {
        return new SearchCourseResponse(
                course.getId(),
                course.getName(),
                course.getCode(),
                course.getDescription(),
                course.getStatus(),
                course.getCreatedAt(),
                course.getInactivatedAt(),
                course.getInstructorId()
        );
    }
}

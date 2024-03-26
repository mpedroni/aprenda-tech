package com.mpedroni.aprendatech.domain.courses.usecases;

import com.mpedroni.aprendatech.domain.courses.exceptions.CourseNotFoundException;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;

public class InactivateCourseUseCase {
    private final CourseJpaRepository courseRepository;

    public InactivateCourseUseCase(CourseJpaRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(String code) {
        var course = courseRepository.findByCode(code).orElseThrow(() -> new CourseNotFoundException(code));
        course.inactivate();
        courseRepository.save(course);
    }
}

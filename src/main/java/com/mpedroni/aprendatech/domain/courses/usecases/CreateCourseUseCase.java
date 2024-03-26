package com.mpedroni.aprendatech.domain.courses.usecases;

import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;

public class CreateCourseUseCase {
    private final CourseJpaRepository courseRepository;

    public CreateCourseUseCase(CourseJpaRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    public CourseJpaEntity execute(CreateCourseCommand command) {
        var course = new CourseJpaEntity(
                command.name(),
                command.code(),
                command.description(),
                command.instructorId()
        );

        return courseRepository.save(course);
    }
}
